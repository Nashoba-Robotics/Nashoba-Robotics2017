package edu.nr.lib.units;

public class Jerk {

	public static final Jerk ZERO = new Jerk(Acceleration.ZERO, Time.ONE_SECOND);
	private Acceleration speed;
	private Time time;

	public Jerk(Acceleration speed, Time time) {
		this.speed = speed;
		this.time = time;
	}

	public Jerk(double val, Distance.Unit angleUnit, Time.Unit timeUnitOne, Time.Unit timeUnitTwo, Time.Unit timeUnitThree) {
		this.speed = new Acceleration(val, angleUnit, timeUnitOne, timeUnitTwo);
		this.time = new Time(1, timeUnitThree);
	}


	public double get(Distance.Unit toAngleUnit, Time.Unit toTimeUnitOne, Time.Unit toTimeUnitTwo, Time.Unit toTimeUnitThree) {
		return speed.get(toAngleUnit, toTimeUnitOne, toTimeUnitTwo) / time.get(toTimeUnitThree);
	}

	public double getDefault() {
		return get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit,Time.Unit.defaultUnit);
	}

	
	public Jerk sub(Jerk angleTwo) {
		return new Jerk(
				speed.mul(angleTwo.time.getDefault())
						.sub(angleTwo.speed.mul(time.getDefault())),
				time.mul(angleTwo.time.getDefault()));
	}

	public Jerk add(Jerk angleTwo) {
		return new Jerk(
				speed.mul(angleTwo.time.getDefault())
						.add(angleTwo.speed.mul(time.getDefault())),
				time.mul(angleTwo.time.getDefault()));
	}

	public Jerk mul(double x) {
		return new Jerk(speed.mul(x), time);
	}
	
	public double div(Jerk other) {
		return speed.div(other.speed) / time.div(other.time);
	}

	public boolean lessThan(Jerk angleTwo) {
		return this.getDefault() < angleTwo.getDefault();
	}

	public boolean greaterThan(Jerk angleTwo) {
		return this.getDefault() > angleTwo.getDefault();
	}

	public Jerk negate() {
		return new Jerk(speed.negate(), time);
	}

	public Jerk abs() {
		return new Jerk(speed.abs(), time.abs());
	}

	@Override
	public boolean equals(Object otherAngle) {
		if (otherAngle instanceof Jerk) {
			return this.getDefault() == ((Jerk) otherAngle).getDefault();
		} else {
			return false;
		}
	}

}
