package edu.nr.lib.units;

public class AngularSpeed {

	public static final AngularSpeed ZERO = new AngularSpeed(Angle.ZERO, Time.ONE_SECOND);
	private Angle angle;
	private Time time;

	public AngularSpeed(Angle angle, Time time) {
		this.angle = angle;
		this.time = time;
	}

	public AngularSpeed(double val, Angle.Unit angleUnit, Time.Unit timeUnit) {
		this.angle = new Angle(val, angleUnit);
		this.time = new Time(1, timeUnit);
	}

	public AngularSpeed(Speed speed) {
		new AngularSpeed(speed.get(Distance.Unit.DRIVE_ROTATION, Time.Unit.MINUTE), Angle.Unit.ROTATION, Time.Unit.MINUTE);
	}

	public double get(Angle.Unit toAngleUnit, Time.Unit toTimeUnit) {
		return angle.get(toAngleUnit) / time.get(toTimeUnit);
	}
	
	public double getDefault() {
		return get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public AngularSpeed sub(AngularSpeed angleTwo) {
		return new AngularSpeed(
				angle.mul(angleTwo.time.get(Time.Unit.SECOND))
						.sub(angleTwo.angle.mul(time.get(Time.Unit.SECOND))),
				time.mul(angleTwo.time.get(Time.Unit.SECOND)));
	}

	public AngularSpeed add(AngularSpeed angleTwo) {
		return new AngularSpeed(
				angle.mul(angleTwo.time.get(Time.Unit.SECOND))
						.add(angleTwo.angle.mul(time.get(Time.Unit.SECOND))),
				time.mul(angleTwo.time.get(Time.Unit.SECOND)));
	}

	public AngularSpeed mul(double x) {
		return new AngularSpeed(angle.mul(x), time);
	}
	
	public double div(AngularSpeed other) {
		return angle.div(other.angle) / time.div(other.time);
	}

	public boolean lessThan(AngularSpeed angleTwo) {
		return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit) < angleTwo.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public boolean greaterThan(AngularSpeed angleTwo) {
		return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit) > angleTwo.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public AngularSpeed negate() {
		return new AngularSpeed(angle.negate(), time);
	}

	public AngularSpeed abs() {
		return new AngularSpeed(angle.abs(), time.abs());
	}

	@Override
	public boolean equals(Object otherAngle) {
		if (otherAngle instanceof AngularSpeed) {
			return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit) == ((AngularSpeed) otherAngle).get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit);
		} else {
			return false;
		}
	}

}
