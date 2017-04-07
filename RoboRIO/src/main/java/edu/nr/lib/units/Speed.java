package edu.nr.lib.units;

import edu.nr.lib.units.Angle.Unit;

public class Speed {

	public static final Speed ZERO = new Speed(Distance.ZERO, Time.ONE_SECOND);
	private Distance distance;
	private Time time;

	public Speed(Distance distance, Time time) {
		this.distance = distance;
		this.time = time;
	}

	public Speed(double val, Distance.Unit distanceUnit, Time.Unit timeUnit) {
		this.distance = new Distance(val, distanceUnit);
		this.time = new Time(1, timeUnit);
	}

	public Speed(AngularSpeed speed) {
		new Speed(speed.get(Angle.Unit.ROTATION, Time.Unit.MINUTE), Distance.Unit.DRIVE_ROTATION, Time.Unit.MINUTE);
	}

	public double get(Distance.Unit toDistanceUnit, Time.Unit toTimeUnit) {
		return distance.get(toDistanceUnit) / time.get(toTimeUnit);
	}
	
	public double getDefault() {
		return get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public Speed sub(Speed speedTwo) {
		return this.add(speedTwo.negate());
	}

	public Speed add(Speed speedTwo) {
		return new Speed(this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit) + speedTwo.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit),Distance.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public Speed mul(double x) {
		return new Speed(distance.mul(x), time);
	}

	public Distance mul(Time timeTwo) {
		return distance.mul(timeTwo.div(time));
	}

	public double div(Speed speedTwo) {
		return distance.div(speedTwo.distance) / time.div(speedTwo.time);
	}

	public boolean lessThan(Speed speedTwo) {
		return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit) < speedTwo.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public boolean greaterThan(Speed speedTwo) {
		return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit) > speedTwo.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public Speed negate() {
		return new Speed(distance.negate(), time);
	}

	public Speed abs() {
		return new Speed(distance.abs(), time.abs());
	}

	public double signum() {
		return Math.signum(distance.get(Distance.Unit.defaultUnit));
	}
	
	@Override
	public boolean equals(Object speedTwo) {
		if (speedTwo instanceof Speed) {
			return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit) == ((Speed) speedTwo).get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit);
		} else {
			return false;
		}
	}

}
