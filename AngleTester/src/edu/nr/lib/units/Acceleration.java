package edu.nr.lib.units;

public class Acceleration {

	public static final Acceleration ZERO = new Acceleration(Speed.ZERO, Time.ONE_SECOND);
	private Speed speed;
	private Time time;

	public Acceleration(Speed speed, Time time) {
		this.speed = speed;
		this.time = time;
	}

	public Acceleration(double val, Distance.Unit angleUnit, Time.Unit timeUnitOne, Time.Unit timeUnitTwo) {
		this.speed = new Speed(val, angleUnit, timeUnitOne);
		this.time = new Time(1, timeUnitTwo);
	}


	public double get(Distance.Unit toAngleUnit, Time.Unit toTimeUnitOne, Time.Unit toTimeUnitTwo) {
		return speed.get(toAngleUnit, toTimeUnitOne) / time.get(toTimeUnitTwo);
	}
	
	public double getDefault() {
		return get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public Acceleration sub(Acceleration angleTwo) {
		return new Acceleration(
				speed.mul(angleTwo.time.get(Time.Unit.SECOND))
						.sub(angleTwo.speed.mul(time.get(Time.Unit.SECOND))),
				time.mul(angleTwo.time.get(Time.Unit.SECOND)));
	}

	public Acceleration add(Acceleration angleTwo) {
		return new Acceleration(
				speed.mul(angleTwo.time.get(Time.Unit.SECOND))
						.add(angleTwo.speed.mul(time.get(Time.Unit.SECOND))),
				time.mul(angleTwo.time.get(Time.Unit.SECOND)));
	}

	public Acceleration mul(double x) {
		return new Acceleration(speed.mul(x), time);
	}
	
	public double div(Acceleration other) {
		return speed.div(other.speed) / time.div(other.time);
	}

	public boolean lessThan(Acceleration angleTwo) {
		return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit) < angleTwo.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
	}

	public boolean greaterThan(Acceleration angleTwo) {
		return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit) > angleTwo.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
	}

	public Acceleration negate() {
		return new Acceleration(speed.negate(), time);
	}

	public Acceleration abs() {
		return new Acceleration(speed.abs(), time.abs());
	}

	@Override
	public boolean equals(Object otherAngle) {
		if (otherAngle instanceof Acceleration) {
			return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit) == ((Acceleration) otherAngle).get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
		} else {
			return false;
		}
	}

}
