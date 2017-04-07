package edu.nr.lib.units;

import edu.nr.lib.units.Angle.Unit;

public class AngularAcceleration {

	public static final AngularAcceleration ZERO = new AngularAcceleration(AngularSpeed.ZERO, Time.ONE_SECOND);
	private AngularSpeed angularSpeed;
	private Time time;

	public AngularAcceleration(AngularSpeed angularSpeed, Time time) {
		this.angularSpeed = angularSpeed;
		this.time = time;
	}

	public AngularAcceleration(double val, Angle.Unit angleUnit, Time.Unit timeUnitOne, Time.Unit timeUnitTwo) {
		this.angularSpeed = new AngularSpeed(val, angleUnit, timeUnitOne);
		this.time = new Time(1, timeUnitTwo);
	}


	public double get(Angle.Unit toAngleUnit, Time.Unit toTimeUnitOne, Time.Unit toTimeUnitTwo) {
		return angularSpeed.get(toAngleUnit, toTimeUnitOne) / time.get(toTimeUnitTwo);
	}
	
	public double getDefault() {
		return get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public AngularAcceleration sub(AngularAcceleration angularAccelerationTwo) {
		return this.add(angularAccelerationTwo.negate());
	}

	public AngularAcceleration add(AngularAcceleration angularAccelerationTwo) {
		return new AngularAcceleration(
				this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit)
				+ angularAccelerationTwo.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit)
				,Angle.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public AngularAcceleration mul(double x) {
		return new AngularAcceleration(angularSpeed.mul(x), time);
	}
	
	public double div(AngularAcceleration angularAccelerationTwo) {
		return angularSpeed.div(angularAccelerationTwo.angularSpeed) / time.div(angularAccelerationTwo.time);
	}

	public boolean lessThan(AngularAcceleration angularAccelerationTwo) {
		return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit) < angularAccelerationTwo.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
	}

	public boolean greaterThan(AngularAcceleration angularAccelerationTwo) {
		return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit) > angularAccelerationTwo.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
	}

	public AngularAcceleration negate() {
		return new AngularAcceleration(angularSpeed.negate(), time);
	}

	public AngularAcceleration abs() {
		return new AngularAcceleration(angularSpeed.abs(), time.abs());
	}

	public double signum() {
		return Math.signum(this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit));
	}
	
	@Override
	public boolean equals(Object angularAccelerationTwo) {
		if (angularAccelerationTwo instanceof AngularAcceleration) {
			return this.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit) == ((AngularAcceleration) angularAccelerationTwo).get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
		} else {
			return false;
		}
	}

}
