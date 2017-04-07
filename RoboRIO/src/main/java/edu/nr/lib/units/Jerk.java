package edu.nr.lib.units;

import edu.nr.lib.units.Angle.Unit;

public class Jerk {

	public static final Jerk ZERO = new Jerk(Acceleration.ZERO, Time.ONE_SECOND);
	private Acceleration acceleration;
	private Time time;

	public Jerk(Acceleration acceleration, Time time) {
		this.acceleration = acceleration;
		this.time = time;
	}

	public Jerk(double val, Distance.Unit distanceUnit, Time.Unit timeUnitOne, Time.Unit timeUnitTwo, Time.Unit timeUnitThree) {
		this.acceleration = new Acceleration(val, distanceUnit, timeUnitOne, timeUnitTwo);
		this.time = new Time(1, timeUnitThree);
	}


	public double get(Distance.Unit toDistanceUnit, Time.Unit toTimeUnitOne, Time.Unit toTimeUnitTwo, Time.Unit toTimeUnitThree) {
		return acceleration.get(toDistanceUnit, toTimeUnitOne, toTimeUnitTwo) / time.get(toTimeUnitThree);
	}

	public double getDefault() {
		return get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit,Time.Unit.defaultUnit);
	}

	
	public Jerk sub(Jerk jerkTwo) {
		return new Jerk(
				acceleration.mul(jerkTwo.time.getDefault())
						.sub(jerkTwo.acceleration.mul(time.getDefault())),
				time.mul(jerkTwo.time.getDefault()));
	}

	public Jerk add(Jerk jerkTwo) {
		return new Jerk(
				acceleration.mul(jerkTwo.time.getDefault())
						.add(jerkTwo.acceleration.mul(time.getDefault())),
				time.mul(jerkTwo.time.getDefault()));
	}

	public Jerk mul(double x) {
		return new Jerk(acceleration.mul(x), time);
	}
	
	public double div(Jerk jerkTwo) {
		return acceleration.div(jerkTwo.acceleration) / time.div(jerkTwo.time);
	}

	public boolean lessThan(Jerk jerkTwo) {
		return this.getDefault() < jerkTwo.getDefault();
	}

	public boolean greaterThan(Jerk jerkTwo) {
		return this.getDefault() > jerkTwo.getDefault();
	}

	public Jerk negate() {
		return new Jerk(acceleration.negate(), time);
	}

	public Jerk abs() {
		return new Jerk(acceleration.abs(), time.abs());
	}

	public double signum() {
		return Math.signum(acceleration.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit));
	}
	
	@Override
	public boolean equals(Object jerkTwo) {
		if (jerkTwo instanceof Jerk) {
			return this.getDefault() == ((Jerk) jerkTwo).getDefault();
		} else {
			return false;
		}
	}

}
