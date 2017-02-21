package edu.nr.lib.units;

public class AngularAcceleration {

	public static final AngularAcceleration ZERO = new AngularAcceleration(AngularSpeed.ZERO, Time.ONE_SECOND);
	private AngularSpeed speed;
	private Time time;

	public AngularAcceleration(AngularSpeed speed, Time time) {
		this.speed = speed;
		this.time = time;
	}

	public AngularAcceleration(double val, Angle.Unit angleUnit, Time.Unit timeUnitOne, Time.Unit timeUnitTwo) {
		this.speed = new AngularSpeed(val, angleUnit, timeUnitOne);
		this.time = new Time(1, timeUnitTwo);
	}


	public double get(Angle.Unit toAngleUnit, Time.Unit toTimeUnitOne, Time.Unit toTimeUnitTwo) {
		return speed.get(toAngleUnit, toTimeUnitOne) / time.get(toTimeUnitTwo);
	}

	public AngularAcceleration sub(AngularAcceleration angleTwo) {
		return new AngularAcceleration(
				speed.mul(angleTwo.time.get(Time.Unit.SECOND))
						.sub(angleTwo.speed.mul(time.get(Time.Unit.SECOND))),
				time.mul(angleTwo.time.get(Time.Unit.SECOND)));
	}

	public AngularAcceleration add(AngularAcceleration angleTwo) {
		return new AngularAcceleration(
				speed.mul(angleTwo.time.get(Time.Unit.SECOND))
						.add(angleTwo.speed.mul(time.get(Time.Unit.SECOND))),
				time.mul(angleTwo.time.get(Time.Unit.SECOND)));
	}

	public AngularAcceleration mul(double x) {
		return new AngularAcceleration(speed.mul(x), time);
	}
	
	public double div(AngularAcceleration other) {
		return speed.div(other.speed) / time.div(other.time);
	}

	public boolean lessThan(AngularAcceleration angleTwo) {
		return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit) < angleTwo.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
	}

	public boolean greaterThan(AngularAcceleration angleTwo) {
		return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit) > angleTwo.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
	}

	public AngularAcceleration negate() {
		return new AngularAcceleration(speed.negate(), time);
	}

	public AngularAcceleration abs() {
		return new AngularAcceleration(speed.abs(), time.abs());
	}

	@Override
	public boolean equals(Object otherAngle) {
		if (otherAngle instanceof AngularAcceleration) {
			return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit) == ((AngularAcceleration) otherAngle).get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
		} else {
			return false;
		}
	}

}
