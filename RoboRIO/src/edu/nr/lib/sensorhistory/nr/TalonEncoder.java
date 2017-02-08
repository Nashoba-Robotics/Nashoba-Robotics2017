package edu.nr.lib.sensorhistory.nr;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.ctre.CANTalon;

public class TalonEncoder extends TimerTask {

	private final Timer timer;

	// In milliseconds
	private final long period;
	private static final long defaultPeriod = 5; // 200 Hz

	CANTalon talon;

	ArrayList<Data> data;

	public TalonEncoder(CANTalon talon) {
		this.talon = talon;

		this.period = defaultPeriod;

		timer = new Timer();
		timer.schedule(this, 0, this.period);

		data = new ArrayList<>();
	}

	@Override
	public void run() {
		data.add(new Data(talon.getPosition(), talon.getSpeed(),
				(long) (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() / 1000.0)));
	}

	/**
	 * Get the position at a time in the past.
	 * 
	 * @param deltaTime
	 *            How long ago to look, in milliseconds
	 * @return the position
	 */
	public double getPosition(long deltaTime) {

		if (data.size() == 0) {
			return talon.getPosition();
		} else if (data.size() == 1) {
			return data.get(0).position;
		}

		long timestamp = (long) (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() / 1000.0) - deltaTime;

		int low = 0;
		int up = data.size();
		while (low < up)
		// @loop_invariant 0 <= low && low <= up && up <= n;
		// @loop_invariant low == 0 || A[low-1] < x;
		// @loop_invariant up == n || A[up] >= x;
		{
			// int mid = (low + up)/2; CAUSES OVERFLOW
			int mid = low + (up - low) / 2;
			if (timestamp == data.get(mid).timestamp)
				return data.get(mid).position;
			else if (timestamp < data.get(mid).timestamp)
				up = mid;
			else
				low = mid + 1;
		}
		low = up - 1; // This is so that low != up

		Data first = data.get(low);
		Data second = data.get(up);
		return interpolate(first.position, second.position, timestamp / (second.timestamp + first.timestamp));
	}

	private double interpolate(double first, double second, double timeRatio) {
		return first * (1 - timeRatio) + second * timeRatio;
	}

	private class Data {
		double position;
		double velocity;

		/**
		 * The time that this was set, in milliseconds
		 */
		long timestamp;

		public Data(double position, double velocity, long timestamp) {
			this.position = position;
			this.velocity = velocity;
			this.timestamp = timestamp;
		}
	}

}
