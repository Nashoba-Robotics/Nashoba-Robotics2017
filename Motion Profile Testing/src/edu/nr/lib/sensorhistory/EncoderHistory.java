package edu.nr.lib.sensorhistory;

import java.util.ArrayList;

import com.kauailabs.sf2.quantity.IQuantity;
import com.kauailabs.sf2.quantity.Scalar;
import com.kauailabs.sf2.sensor.ISensorDataSource;
import com.kauailabs.sf2.sensor.ISensorDataSubscriber;
import com.kauailabs.sf2.sensor.ISensorInfo;
import com.kauailabs.sf2.sensor.SensorDataSourceInfo;
import com.kauailabs.sf2.time.ThreadsafeInterpolatingTimeHistory;
import com.kauailabs.sf2.time.Timestamp;
import com.kauailabs.sf2.time.TimestampedValue;

/**
 * The OrientationHistory class implements a timestamped history of orientation
 * data (e.g., from an IMU). The OrientationHistory is populated by data from a
 * "timestamped encoder" sensor, such as the navX-MXP.
 * 
 * The OrientationHistory buffers the orientation data received over the most
 * current time period between "now" and the size of the time history, and
 * provides methods to retrieve orientation data in the form of
 * TimestampedEncoder objects. These objects can be looked up based upon a
 * timestamp; if an exact match is found the object is returned direction;
 * otherwise if TimestampedEncoder objects exist for the times before and
 * after the requested timestamp, a new TimestampedEncoder object is created
 * via interpolation.
 * 
 * @author Scott
 */
public class EncoderHistory implements ISensorDataSubscriber {

	ISensorDataSource enc;
	ThreadsafeInterpolatingTimeHistory<TimestampedValue<EncoderPosition>> orientation_history;
	Scalar temp_s;
	int encoder_quantity_index;
	int timestamp_quantity_index;
	TimestampedValue<EncoderPosition> temp_tsq;
	Timestamp system_timestamp;
	
	public final int MAX_ORIENTATION_HISTORY_LENGTH_NUM_SAMPLES = 1000;

	/**
	 * Constructs an OrientationHistory object with a specified size. The
	 * OrientationHistory registers for incoming data using the provided
	 * ITimestampedEncoderSensor object.
	 * 
	 * @param enc_sensor
	 *            - the sensor to acquire TimestampedEncoder objects from.
	 * @param history_length_seconds
	 *            - the length of the OrientationHistory, in seconds. The actual
	 *            length of the OrientationHistory in number of objects is
	 *            calculated internally by accessing the sensor's current update
	 *            rate. <i>Note: if the sensor update rate is changed, after
	 *            this constructor is invoked, the length of the history may no
	 *            longer accurately reflect the originally-configured
	 *            length.</i>
	 * @param enc_sensor
	 *            - the sensor to use as the source of TimestampedEncoders
	 *            contained in the Orientation History
	 * @param history_length_seconds
	 *            - the number of seconds the history will represent. This value
	 *            may not be larger than @value
	 *            #MAX_ORIENTATION_HISTORY_IN_SECONDS seconds.
	 */
	public EncoderHistory(ISensorInfo enc, int history_length_num_samples) {

		this.enc = enc.getSensorDataSource();

		int index = 0;
		encoder_quantity_index = -1;
		timestamp_quantity_index = -1;
		ArrayList<SensorDataSourceInfo> sensor_data_source_infos = new ArrayList<SensorDataSourceInfo>();
		enc.getSensorDataSource().getSensorDataSourceInfos(sensor_data_source_infos);
		for (SensorDataSourceInfo item : sensor_data_source_infos) {
			if (item.getName().equalsIgnoreCase("CANTalon")) {
				encoder_quantity_index = index;
			}
			if (item.getName().equalsIgnoreCase("Timestamp")) {
				timestamp_quantity_index = index;
			}
			index++;
		}

		if (encoder_quantity_index == -1) {
			throw new IllegalArgumentException("The provided ISensorInfo (enc_sensor) object"
					+ "must contain a SensorDataSourceInfo object named 'CANTalon'.");
		}

		if (history_length_num_samples > MAX_ORIENTATION_HISTORY_LENGTH_NUM_SAMPLES) {
			history_length_num_samples = MAX_ORIENTATION_HISTORY_LENGTH_NUM_SAMPLES;
		}
		EncoderPosition default_enc = new EncoderPosition();
		TimestampedValue<EncoderPosition> default_ts_enc = new TimestampedValue<EncoderPosition>(default_enc);
		this.orientation_history = new ThreadsafeInterpolatingTimeHistory<TimestampedValue<EncoderPosition>>(default_ts_enc,
				history_length_num_samples, enc.getSensorTimestampInfo(),
				sensor_data_source_infos.get(encoder_quantity_index).getName(),
				sensor_data_source_infos.get(encoder_quantity_index).getQuantityUnits());

		this.enc.subscribe(this);

		temp_s = new Scalar();
		
		temp_tsq = new TimestampedValue<EncoderPosition>(new EncoderPosition());
		
		system_timestamp = new Timestamp();
	}

	/**
	 * Reset the OrientationHistory, clearing all existing entries.
	 * 
	 * @param enc_curr
	 */
	public void reset(TimestampedValue<EncoderPosition> enc_curr) {
		orientation_history.reset();
	}

	/**
	 * Retrieves the most recently added Encoder.
	 * 
	 * @return
	 */
	public boolean getCurrentEncoder(TimestampedValue<EncoderPosition> out) {
		return orientation_history.getMostRecent(out);
	}

	/**
	 * Retrieves the TimestampedEncoder at the specified sensor timestamp. If
	 * an exact timestamp match occurs, a TimestampedEncoder representing the
	 * actual (measured) data is returned; otherwise a new interpolated
	 * TimestampedEncoder will be estimated, using the nearest
	 * preceding/following TimestampedEncoder and the requested timestamp's
	 * ratio of time between them as its basis. If no exact match could be found
	 * or interpolated value estimated, null is returned.
	 * 
	 * @param requested_timestamp
	 *            - sensor timestamp to retrieve
	 * @return TimestampedEncoder at requested timestamp, or null.
	 */
	public boolean getEncoderAtTime(long requested_timestamp, TimestampedValue<EncoderPosition> out) {
		return orientation_history.get(requested_timestamp, out);
	}

	/**
	 * Retrieves the position at the specified sensor timestamp.
	 * <p>
	 * Note that this value may be interpolated if a sample at the requested
	 * time is not available.
	 * 
	 * @param requested_timestamp
	 * @return Position at the requested
	 *         timestamp. If a roll angle at the specified timestamp could not
	 *         be found/interpolated, the value INVALID_POSITION (NaN) will be
	 *         returned.
	 */
	public float getPositionAtTime(long requested_timestamp) {
		TimestampedValue<EncoderPosition> match = new TimestampedValue<EncoderPosition>(new EncoderPosition());
		if (getEncoderAtTime(requested_timestamp, match)) {
			ArrayList<IQuantity> quantities = new ArrayList<>();
			match.getQuantity().getContainedQuantities(quantities);
			return ((Scalar) quantities.get(0)).get(); //Only has position
		} else {
			return Float.NaN;
		}
	}

	@Override
	public void publish(IQuantity[] curr_values, Timestamp sys_timestamp) {
		Timestamp sensor_timestamp;
		if ( timestamp_quantity_index != -1 ) {
			sensor_timestamp = ((Timestamp)curr_values[timestamp_quantity_index]);
		} else {
			sensor_timestamp = sys_timestamp;
		}
		EncoderPosition q = ((EncoderPosition) curr_values[encoder_quantity_index]);
		temp_tsq.set(q,  sensor_timestamp.getMilliseconds());
		orientation_history.add(temp_tsq);
	}

	public boolean writeToDirectory(String directory_path) {
		return orientation_history.writeToDirectory(directory_path);
	}
	
	public boolean writeToFile(String file_path){
		return orientation_history.writeToFile(file_path);
	}
	
}
