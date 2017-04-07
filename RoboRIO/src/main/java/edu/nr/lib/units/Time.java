package edu.nr.lib.units;

public class Time {
	
	public static final Time ZERO = new Time(0, Unit.defaultUnit);
	public static final Time ONE_SECOND = new Time(1, Unit.SECOND);
	private double val;
	private Unit type;
	
	public enum Unit implements GenericUnit {
		SECOND, MINUTE, MILLISECOND, HUNDRED_MILLISECOND;
		
		public static final Unit defaultUnit = SECOND;
		
		private static final double MILLISECONDS_PER_SECOND = 1000;
		private static final double HUNDRED_MILLISECONDS_PER_SECOND = 10;
		private static final double MINUTES_PER_SECOND = 1/60.0;
				
		public double convertToDefault(double val) {
			if(this == Unit.defaultUnit) {
				return val;
			}
			if(this == Unit.MINUTE) {
				return val / MINUTES_PER_SECOND;
			}
			if(this == Unit.MILLISECOND) {
				return val / MILLISECONDS_PER_SECOND;
			}
			if(this == HUNDRED_MILLISECOND) {
				return val / HUNDRED_MILLISECONDS_PER_SECOND;
			}
			return 0;
		}
		
		public double convertFromDefault(double val) {
			if(this == Unit.defaultUnit) {
				return val;
			}
			if(this == Unit.MINUTE) {
				return MINUTES_PER_SECOND * val;
			}
			if(this == Unit.MILLISECOND) {
				return MILLISECONDS_PER_SECOND * val;
			}
			if(this == Unit.HUNDRED_MILLISECOND) {
				return val * HUNDRED_MILLISECONDS_PER_SECOND;
			}
			return 0;
		}
}
	
	public Time(double val, Unit type) {
		this.val = val;
		this.type = type;
	}
	
	public double get(Unit toType) {
		return type.convert(val, toType);
	}
	
	public Time sub(Time timeTwo) {
		return new Time(this.get(Unit.defaultUnit) - timeTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Time add(Time timeTwo) {
		return new Time(this.get(Unit.defaultUnit) + timeTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Time mul(double x) {
		return new Time(this.get(Unit.defaultUnit) * x, Unit.defaultUnit);
	}

	public Distance mul(Speed speed) {
		return speed.mul(this);
	}

	public double div(Time time) {
		return this.get(Unit.defaultUnit) / time.get(Unit.defaultUnit);
	}
	
	public boolean lessThan(Time timeTwo) {
		return this.get(Unit.defaultUnit) < timeTwo.get(Unit.defaultUnit);
	}

	public boolean greaterThan(Time timeTwo) {
		return this.get(Unit.defaultUnit) > timeTwo.get(Unit.defaultUnit);
	}
	
	public Time negate() {
		return new Time(-this.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Time abs() {
		return new Time(Math.abs(this.get(Unit.defaultUnit)), Unit.defaultUnit);
	}
	
	public static Time getCurrentTime() {
		return new Time(edu.wpi.first.wpilibj.Timer.getFPGATimestamp(), Time.Unit.SECOND);
	}
	
	@Override
	public boolean equals(Object timeTwo) {
		if(timeTwo instanceof Time) {
			return this.get(Unit.defaultUnit) == ((Time) timeTwo).get(Unit.defaultUnit);
		} else {
			return false;
		}
	}

	public double getDefault() {
		return get(Unit.defaultUnit);
	}

}
