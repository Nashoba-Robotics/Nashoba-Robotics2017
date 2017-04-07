package edu.nr.lib.units;

public class Acceleration {

	public static final Acceleration ZERO = new Acceleration(Speed.ZERO, Time.ONE_SECOND);
	private Speed speed;
	private Time time;

	public Acceleration(Speed speed, Time time) {
		this.speed = speed;
		this.time = time;
	}

	public Acceleration(double val, Distance.Unit distanceUnit, Time.Unit timeUnitOne, Time.Unit timeUnitTwo) {
		this.speed = new Speed(val, distanceUnit, timeUnitOne);
		this.time = new Time(1, timeUnitTwo);
	}


	public double get(Distance.Unit toDistanceUnit, Time.Unit toTimeUnitOne, Time.Unit toTimeUnitTwo) {
		return speed.get(toDistanceUnit, toTimeUnitOne) / time.get(toTimeUnitTwo);
	}
	
	public double getDefault() {
		return get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public Acceleration sub(Acceleration accelerationTwo) {
		return this.add(accelerationTwo.negate());
	}

	public Acceleration add(Acceleration accelerationTwo) {
		return new Acceleration(this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit)
				+ accelerationTwo.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit)
				,Distance.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit);
	}

	public Acceleration mul(double x) {
		return new Acceleration(speed.mul(x), time);
	}
	
	public double div(Acceleration accelerationTwo) {
		return speed.div(accelerationTwo.speed) / time.div(accelerationTwo.time);
	}

	public boolean lessThan(Acceleration accelerationTwo) {
		return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit) < accelerationTwo.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
	}

	public boolean greaterThan(Acceleration accelerationTwo) {
		return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit) > accelerationTwo.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
	}

	public Acceleration negate() {
		return new Acceleration(speed.negate(), time);
	}

	public Acceleration abs() {
		return new Acceleration(speed.abs(), time.abs());
	}

	public double signum() {
		return Math.signum(this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit, Time.Unit.defaultUnit));
	}
	
	@Override
	public boolean equals(Object accelerationTwo) {
		if (accelerationTwo instanceof Acceleration) {
			return this.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit) == ((Acceleration) accelerationTwo).get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit,Time.Unit.defaultUnit);
		} else {
			return false;
		}
	}

}
