package edu.nr.lib;

import java.util.ArrayList;

import com.ctre.CANTalon;
import com.kauailabs.sf2.frc.RoboRIO;
import com.kauailabs.sf2.quantity.IQuantity;
import com.kauailabs.sf2.sensor.IProcessorInfo;
import com.kauailabs.sf2.sensor.ISensorDataSource;
import com.kauailabs.sf2.sensor.ISensorDataSubscriber;
import com.kauailabs.sf2.sensor.ISensorInfo;
import com.kauailabs.sf2.sensor.SensorDataSourceInfo;
import com.kauailabs.sf2.time.Timestamp;
import com.kauailabs.sf2.time.TimestampInfo;
import com.kauailabs.sf2.time.TimestampedValue;
import com.kauailabs.sf2.units.Unit;
import com.kauailabs.sf2.units.Unit.IUnit;

import edu.nr.lib.interfaces.Periodic;
import edu.wpi.first.wpilibj.Timer;


/**
 * An extension of CANTalon that logs TalonSRX data.
 * 
 * Based on the Kauai Labs Sensor Fusion Framework.
 *
 */
public class HistoricalCANTalon extends CANTalon implements Periodic, ISensorDataSource, ISensorInfo {

    EncoderHistory encoder_history;
    
    double last_write_timestamp = 0;
    
    double period = 0.1; //In seconds

	
	public HistoricalCANTalon(int deviceNumber) {
		super(deviceNumber);
		
		this.curr_data = new TimestampedValue<EncoderPosition>(new EncoderPosition());
		this.curr_data.setValid(true);
		this.roborio = new RoboRIO();
		this.sensor_data_source_infos = new ArrayList<SensorDataSourceInfo>();
		this.enc_callback_registered = false;
		Timestamp ts = new Timestamp(0, Timestamp.TimestampResolution.Millisecond);
		enc_tsinfo = new TimestampInfo(TimestampInfo.Scope.Sensor, TimestampInfo.Basis.SinceLastReboot,
				1.0 / Timestamp.MILLISECONDS_PER_SECOND, /* Resolution */
				1.0 / Timestamp.MILLISECONDS_PER_SECOND, /* Accuracy */
				1.0 / (360 * Timestamp.MILLISECONDS_PER_SECOND), /*
																	 * Clock Drift
																	 * - seconds
																	 * per hour
																	 */
				1.0 / Timestamp.MILLISECONDS_PER_SECOND, /* Average Latency */
				ts); /* Clock drift/hour */
		this.sensor_data_source_infos.add(
				new SensorDataSourceInfo("Timestamp", ts, new IUnit[] {new Unit().new Time().new Milliseconds()}));
		this.sensor_data_source_infos.add(new SensorDataSourceInfo("Position", new EncoderPosition(), EncoderPosition.getUnits()));
		SensorDataSourceInfo[] data_source_infos = ((SensorDataSourceInfo[]) sensor_data_source_infos
				.toArray(new SensorDataSourceInfo[sensor_data_source_infos.size()]));
		ArrayList<IQuantity> quantity_list = new ArrayList<IQuantity>();
		SensorDataSourceInfo.getQuantityArray(data_source_infos, quantity_list);
		active_sensor_data_quantities = (IQuantity[]) quantity_list.toArray(new IQuantity[quantity_list.size()]);
		tsq_subscribers = new ArrayList<ISensorDataSubscriber>();
		roborio_timestamp = new Timestamp();
		mutex = new Object();
		
		encoder_history = new EncoderHistory(this, 1);
		
		periodics.add(this);
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					Timer.delay(period); //The time in between values
					timestampedDataReceived((long) (1000*Timer.getFPGATimestamp()),(float) getPosition());
				}
			}
			
		}).start();
		
	}

	@Override
	public void periodic() {
    	if ((Timer.getFPGATimestamp() - last_write_timestamp) > 5.0) {
    		encoder_history.writeToDirectory("/home/lvuser/sf2");
            last_write_timestamp = Timer.getFPGATimestamp();
    	}
	}
	
	RoboRIO roborio;
	ArrayList<SensorDataSourceInfo> sensor_data_source_infos;
	TimestampedValue<EncoderPosition> curr_data;
	long last_system_timestamp;
	long last_sensor_timestamp;
	ArrayList<ISensorDataSubscriber> tsq_subscribers;
	boolean enc_callback_registered;
	IQuantity[] active_sensor_data_quantities;
	TimestampInfo enc_tsinfo;
	Timestamp roborio_timestamp;
	Object mutex;
	
	public static int QUANTITY_INDEX_POSITION = 0;

	@Override
	public boolean subscribe(ISensorDataSubscriber subscriber) {
		synchronized (mutex) {
			if (tsq_subscribers.contains(subscriber)) {
				return false;
			}
		}
		synchronized (mutex) {
			tsq_subscribers.add(subscriber);
		}
		return true;
	}

	@Override
	public boolean unsubscribe(ISensorDataSubscriber subscriber) {
		boolean unsubscribed = false;
		synchronized (mutex) {
			unsubscribed = tsq_subscribers.remove(subscriber);
		}
		return unsubscribed;
	}

	/**
	 * 
	 * @param sensor_timestamp in milliseconds
	 * @param position
	 */
	public void timestampedDataReceived(long sensor_timestamp, float position) {
		((Timestamp) active_sensor_data_quantities[0]).setTimestamp(sensor_timestamp);
		((EncoderPosition) active_sensor_data_quantities[1]).set(position);
		
		roborio.getProcessorTimestamp(roborio_timestamp);
		synchronized (mutex) {
			for (ISensorDataSubscriber subscriber : tsq_subscribers) {
				subscriber.publish(active_sensor_data_quantities, roborio_timestamp);
			}
		}
	}

	@Override
	public IProcessorInfo getHostProcessorInfo() {
		return roborio;
	}

	@Override
	public String getMake() {
		return "Cross The Road Electronics + Nashoba Robotics";
	}

	@Override
	public String getModel() {
		return "CAN Talon SRX";
	}

	@Override
	public String getName() {
		return "CANTalon";
	}

	@Override
	public void getSensorDataSourceInfos(ArrayList<SensorDataSourceInfo> infos) {
		infos.addAll(sensor_data_source_infos);
	}

	@Override
	public boolean getCurrent(IQuantity[] quantities, Timestamp ts) {
		((Timestamp) quantities[0]).setTimestamp((long) (1000*Timer.getFPGATimestamp()));
		((EncoderPosition) quantities[1]).set((float) this.getPosition());
		roborio.getProcessorTimestamp(ts);
		return true;
	}

	@Override
	public ISensorDataSource getSensorDataSource() {
		return this;
	}

	@Override
	public TimestampInfo getSensorTimestampInfo() {
		return enc_tsinfo;
	}

	public boolean reset(int index) {
		if (index == QUANTITY_INDEX_POSITION) {
			this.setEncPosition(0);
			return true;
		}
		return false;
	}
	
	@Override
	public void setStatusFrameRateMs(StatusFrameRate stateFrame, int periodMs)
	{
		period = Math.min(periodMs / 1000.0, period);
		super.setStatusFrameRateMs(stateFrame, periodMs);
	}
	
	
	public double getHistoricalPosition(double deltaTime) {
		long encoderTimeStamp = (long) (this.last_write_timestamp - deltaTime);
		return encoder_history.getPositionAtTime(encoderTimeStamp);
	}


}
