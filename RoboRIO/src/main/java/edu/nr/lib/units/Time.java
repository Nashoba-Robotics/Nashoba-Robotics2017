package edu.nr.lib.units;

public class Time {
	
	public static final Time ZERO = new Time(0, Unit.defaultUnit);
	public static final Time ONE_SECOND = new Time(1, Unit.SECOND);
	private double val;
	private Unit type;
	
	public enum Unit {
		SECOND, MINUTE, MILLISECOND, HUNDRED_MILLISECOND;
		
		public static final Unit defaultUnit = SECOND;
		
		private static final double MILLISECONDS_PER_SECOND = 1000;
		private static final double HUNDRED_MILLISECONDS_PER_SECOND = 10;
		private static final double MINUTES_PER_SECOND = 1/60.0;
				
		static public double convertToDefault(double val, Unit fromType) {
			if(fromType == Unit.defaultUnit) {
				return val;
			}
			if(fromType == Unit.MINUTE) {
				return val / MINUTES_PER_SECOND;
			}
			if(fromType == Unit.MILLISECOND) {
				return val / MILLISECONDS_PER_SECOND;
			}
			if(fromType == HUNDRED_MILLISECOND) {
				return val / HUNDRED_MILLISECONDS_PER_SECOND;
			}
			return 0;
		}
		
		static public double convertFromDefault(double val, Unit toType) {
			if(toType == Unit.defaultUnit) {
				return val;
			}
			if(toType == Unit.MINUTE) {
				return MINUTES_PER_SECOND * val;
			}
			if(toType == Unit.MILLISECOND) {
				return MILLISECONDS_PER_SECOND * val;
			}
			if(toType == Unit.HUNDRED_MILLISECOND) {
				return val * HUNDRED_MILLISECONDS_PER_SECOND;
			}
			return 0;
		}

		static public double convert(double val, Unit fromType, Unit toType) {
			if(fromType == toType) {
				return val;
			}
			return convertFromDefault(convertToDefault(val, fromType), toType);
		}
}
	
	public Time(double val, Unit type) {
		this.val = val;
		this.type = type;
	}
	
	public double get(Unit toType) {
		return Unit.convert(val, type, toType);
	}
	
	public Time sub(Time angleTwo) {
		return new Time(this.get(Unit.defaultUnit) - angleTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Time add(Time angleTwo) {
		return new Time(this.get(Unit.defaultUnit) + angleTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Time mul(double x) {
		return new Time(this.get(Unit.defaultUnit) * x, Unit.defaultUnit);
	}

	public Distance mul(Speed speed) {
		return speed.mul(this);
	}

	public double div(Time t) {
		return this.get(Unit.defaultUnit) / t.get(Unit.defaultUnit);
	}
	
	public boolean lessThan(Time angleTwo) {
		return this.get(Unit.defaultUnit) < angleTwo.get(Unit.defaultUnit);
	}

	public boolean greaterThan(Time angleTwo) {
		return this.get(Unit.defaultUnit) > angleTwo.get(Unit.defaultUnit);
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
	public boolean equals(Object otherTime) {
		if(otherTime instanceof Time) {
			return this.get(Unit.defaultUnit) == ((Time) otherTime).get(Unit.defaultUnit);
		} else {
			return false;
		}
	}

}
