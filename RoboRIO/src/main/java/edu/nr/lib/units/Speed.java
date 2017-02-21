package edu.nr.lib.units;

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

	public Speed sub(Speed angleTwo) {
		return new Speed(
				distance.mul(angleTwo.time.get(Time.Unit.SECOND))
						.sub(angleTwo.distance.mul(time.get(Time.Unit.SECOND))),
				time.mul(angleTwo.time.get(Time.Unit.SECOND)));
	}

	public Speed add(Speed angleTwo) {
		return new Speed(
				distance.mul(angleTwo.time.get(Time.Unit.SECOND))
						.add(angleTwo.distance.mul(time.get(Time.Unit.SECOND))),
				time.mul(angleTwo.time.get(Time.Unit.SECOND)));
	}

	public Speed mul(double x) {
		return new Speed(distance.mul(x), time);
	}
	
	public double div(Speed other) {
		return distance.div(other.distance) / time.div(other.time);
	}

	public boolean lessThan(Speed angleTwo) {
		return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit) < angleTwo.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public boolean greaterThan(Speed angleTwo) {
		return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit) > angleTwo.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public Speed negate() {
		return new Speed(distance.negate(), time);
	}

	public Speed abs() {
		return new Speed(distance.abs(), time.abs());
	}

	@Override
	public boolean equals(Object otherAngle) {
		if (otherAngle instanceof Speed) {
			return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit) == ((Speed) otherAngle).get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit);
		} else {
			return false;
		}
	}

}
