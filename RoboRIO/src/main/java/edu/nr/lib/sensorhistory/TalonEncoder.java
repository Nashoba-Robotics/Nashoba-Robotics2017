package edu.nr.lib.sensorhistory;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.ctre.CANTalon;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;

public class TalonEncoder extends TimerTask {

	private final Timer timer;

	// In milliseconds
	private final Time period;
	private static final Time defaultPeriod = new Time(5, Time.Unit.MILLISECOND); // 200
																					// Hz

	CANTalon talon;

	ArrayList<Data> data;

	public TalonEncoder(CANTalon talon) {
		this.talon = talon;

		this.period = defaultPeriod;

		timer = new Timer();
		timer.schedule(this, 0, (long) this.period.get(Time.Unit.MILLISECOND));

		data = new ArrayList<>();
	}

	@Override
	public void run() {
		data.add(new Data(talon.getPosition(),
				new AngularSpeed(talon.getSpeed(), Angle.Unit.ROTATION, Time.Unit.MINUTE), Time.getCurrentTime()));
	}

	/**
	 * Get the position at a time in the past.
	 * 
	 * @param deltaTime
	 *            How long ago to look, in milliseconds
	 * @return the position in rotations
	 */
	public double getPosition(Time deltaTime) {

		if (deltaTime.equals(Time.ZERO)) {
			return talon.getPosition();
		}

		if (data.size() == 0) {
			return talon.getPosition();
		} else if (data.size() == 1) {
			return data.get(0).position;
		}

		Time timestamp = Time.getCurrentTime().sub(deltaTime);

		int low = 0;
		int up = data.size() - 1;
		while (low < up)
		// @loop_invariant 0 <= low && low <= up && up <= n;
		// @loop_invariant low == 0 || A[low-1] < x;
		// @loop_invariant up == n || A[up] >= x;
		{
			// int mid = (low + up)/2; CAUSES OVERFLOW
			int mid = low + (up - low) / 2;
			if (timestamp.equals(data.get(mid).timestamp))
				return data.get(mid).position;
			else if (timestamp.lessThan(data.get(mid).timestamp))
				up = mid;
			else
				low = mid + 1;
		}
		low = up - 1; // This is so that low != up

		if (low == -1) {
			// We haven't been running for long enough.
			return data.get(up).position;
		}

		Data first = data.get(low);
		Data second = data.get(up);
		if (first.timestamp.equals(second.timestamp)) {
			System.out.println("The timestamps are equal in " + this + ". This is weird and unexpected...");
			return 0;
		}
		return interpolate(first.position, second.position, timestamp.div(second.timestamp.add(first.timestamp)));

	}

	/**
	 * Get the velocity at a time in the past.
	 * 
	 * @param deltaTime
	 *            How long ago to look
	 * @return the velocity
	 */
	public AngularSpeed getVelocity(Time deltaTime) {

		if (deltaTime.equals(Time.ZERO)) {
			return new AngularSpeed(talon.getSpeed(), Angle.Unit.ROTATION, Time.Unit.MINUTE);
		}

		if (data.size() == 0) {
			return new AngularSpeed(talon.getSpeed(), Angle.Unit.ROTATION, Time.Unit.MINUTE);
		} else if (data.size() == 1) {
			return data.get(0).velocity;
		}

		Time timestamp = Time.getCurrentTime().sub(deltaTime);

		int low = 0;
		int up = data.size() - 1;
		while (low < up)
		// @loop_invariant 0 <= low && low <= up && up <= n;
		// @loop_invariant low == 0 || A[low-1] < x;
		// @loop_invariant up == n || A[up] >= x;
		{
			// int mid = (low + up)/2; CAUSES OVERFLOW
			int mid = low + (up - low) / 2;
			if (timestamp.equals(data.get(mid).timestamp))
				return data.get(mid).velocity;
			else if (timestamp.lessThan(data.get(mid).timestamp))
				up = mid;
			else
				low = mid + 1;
		}
		low = up - 1; // This is so that low != up

		if (low == -1) {
			// We haven't been running for long enough.
			return data.get(up).velocity;
		}

		Data first = data.get(low);
		Data second = data.get(up);
		if (first.timestamp.equals(second.timestamp)) {
			System.out.println("The timestamps are equal in " + this + ". This is weird and unexpected...");
			return AngularSpeed.ZERO;
		}
		return new AngularSpeed(
				interpolate(first.velocity.getDefault(), second.velocity.getDefault(),
						timestamp.div(second.timestamp.add(first.timestamp))),
				Angle.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	private double interpolate(double first, double second, double timeRatio) {
		return first * (1 - timeRatio) + second * timeRatio;
	}

	private class Data {
		double position;
		AngularSpeed velocity;

		Time timestamp;

		public Data(double position, AngularSpeed velocity, Time timestamp) {
			this.position = position;
			this.velocity = velocity;
			this.timestamp = timestamp;
		}
	}

}
