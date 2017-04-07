package edu.nr.lib.units;

import edu.nr.lib.units.Angle.Unit;

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

	public double get(Angle.Unit toAngleUnit, Time.Unit toTimeUnit) {
		return angle.get(toAngleUnit) / time.get(toTimeUnit);
	}
	
	public double getDefault() {
		return get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public AngularSpeed sub(AngularSpeed angularSpeedTwo) {
		return this.add(angularSpeedTwo.negate());
	}

	public AngularSpeed add(AngularSpeed angularSpeedTwo) {
		return new AngularSpeed(this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit) + angularSpeedTwo.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit),Angle.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public AngularSpeed mul(double x) {
		return new AngularSpeed(angle.mul(x), time);
	}
	
	public double div(AngularSpeed angularSpeedTwo) {
		return angle.div(angularSpeedTwo.angle) / time.div(angularSpeedTwo.time);
	}

	public boolean lessThan(AngularSpeed angularSpeedTwo) {
		return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit) < angularSpeedTwo.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public boolean greaterThan(AngularSpeed angularSpeedTwo) {
		return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit) > angularSpeedTwo.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public AngularSpeed negate() {
		return new AngularSpeed(angle.negate(), time);
	}

	public AngularSpeed abs() {
		return new AngularSpeed(angle.abs(), time.abs());
	}

	public double signum() {
		return Math.signum(this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit));
	}
	
	@Override
	public boolean equals(Object angularSpeedTwo) {
		if (angularSpeedTwo instanceof AngularSpeed) {
			return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit) == ((AngularSpeed) angularSpeedTwo).get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit);
		} else {
			return false;
		}
	}

}
