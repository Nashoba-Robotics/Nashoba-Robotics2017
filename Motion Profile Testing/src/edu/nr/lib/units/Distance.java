package edu.nr.lib.units;

import edu.nr.lib.Units;
import edu.nr.robotics.RobotMap;

public class Distance {
	
	public static final Distance ZERO = new Distance(0, Unit.defaultUnit);
	private double val;
	private Unit type;
	
	public enum Unit {
		FOOT, INCH, DRIVE_ROTATION, METER;
		
		public static final Unit defaultUnit = INCH;
		
		private static final double DRIVE_ROTATION_PER_INCH = RobotMap.WHEEL_DIAMETER * Math.PI;
		private static final double FOOT_PER_INCH = 1.0/Units.INCHES_PER_FOOT;
		private static final double METER_PER_INCH = 1.0/Units.INCHES_PER_METER;
				
		static private double convertToDefault(double val, Unit fromType) {
			if(fromType == Unit.defaultUnit) {
				return val;
			}
			if(fromType == Unit.FOOT) {
				return val / FOOT_PER_INCH;
			}
			if(fromType == Unit.DRIVE_ROTATION) {
				return val / DRIVE_ROTATION_PER_INCH;
			}
			if(fromType == Unit.METER) {
				return val / METER_PER_INCH;
			}
			return 0;
		}
		
		static private double convertFromDefault(double val, Unit toType) {
			if(toType == Unit.defaultUnit) {
				return val;
			}
			if(toType == Unit.FOOT) {
				return FOOT_PER_INCH * val;
			}
			if(toType == Unit.DRIVE_ROTATION) {
				return DRIVE_ROTATION_PER_INCH * val;
			}
			if(toType == Unit.METER) {
				return METER_PER_INCH * val;
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
	
	public Distance(double val, Unit type) {
		this.val = val;
		this.type = type;
	}
	
	public double get(Unit toType) {
		return Unit.convert(val, type, toType);
	}
	
	public Distance sub(Distance angleTwo) {
		return new Distance(this.get(Unit.defaultUnit) - angleTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Distance add(Distance angleTwo) {
		return new Distance(this.get(Unit.defaultUnit) + angleTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Distance mul(double x) {
		return new Distance(this.get(Unit.defaultUnit) * x, Unit.defaultUnit);
	}
	
	public boolean lessThan(Distance angleTwo) {
		return this.get(Unit.defaultUnit) < angleTwo.get(Unit.defaultUnit);
	}

	public boolean greaterThan(Distance angleTwo) {
		return this.get(Unit.defaultUnit) > angleTwo.get(Unit.defaultUnit);
	}
	
	public Distance negate() {
		return new Distance(-this.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Distance abs() {
		return new Distance(Math.abs(this.get(Unit.defaultUnit)), Unit.defaultUnit);
	}
	
	@Override
	public boolean equals(Object otherAngle) {
		if(otherAngle instanceof Distance) {
			return this.get(Unit.defaultUnit) == ((Distance) otherAngle).get(Unit.defaultUnit);
		} else {
			return false;
		}
	}

	public double div(Distance distance) {
		return this.get(Unit.defaultUnit) / distance.get(Unit.defaultUnit);
	}

}
