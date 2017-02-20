package edu.nr.lib.units;

public class Angle {
	
	public static final Angle ZERO = new Angle(0, Unit.DEGREE);
	private double val;
	private Unit type;
	
	public enum Unit {
		DEGREE, ROTATION, RADIAN;
		
		private static final Unit defaultUnit = DEGREE;
		
		private static final double ROTATIONS_PER_DEGREE = 1/360.0;
		private static final double RADIANS_PER_DEGREE = 2*Math.PI / 360.0;
				
		static private double convertToDefault(double val, Unit fromType) {
			if(fromType == Unit.DEGREE) {
				return val;
			}
			if(fromType == Unit.ROTATION) {
				return val / ROTATIONS_PER_DEGREE;
			}
			if(fromType == Unit.RADIAN) {
				return val / RADIANS_PER_DEGREE;
			}
			return 0;
		}
		
		static private double convertFromDefault(double val, Unit toType) {
			if(toType == Unit.DEGREE) {
				return val;
			}
			if(toType == Unit.ROTATION) {
				return ROTATIONS_PER_DEGREE * val;
			}
			if(toType == Unit.RADIAN) {
				return RADIANS_PER_DEGREE * val;
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
	
	public Angle(double val, Unit type) {
		this.val = val;
		this.type = type;
	}
	
	public double get(Unit toType) {
		return Unit.convert(val, type, toType);
	}
	
	public Angle sub(Angle angleTwo) {
		return new Angle(this.get(Unit.defaultUnit) - angleTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Angle add(Angle angleTwo) {
		return new Angle(this.get(Unit.defaultUnit) + angleTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Angle mul(double x) {
		return new Angle(this.get(Unit.defaultUnit) * x, Unit.defaultUnit);
	}
	
	public double cos() {
		return Math.cos(this.get(Unit.RADIAN));
	}
	
	public double sin() {
		return Math.sin(this.get(Unit.RADIAN));
	}
	
	public boolean lessThan(Angle angleTwo) {
		return this.get(Unit.defaultUnit) < angleTwo.get(Unit.defaultUnit);
	}

	public boolean greaterThan(Angle angleTwo) {
		return this.get(Unit.defaultUnit) > angleTwo.get(Unit.defaultUnit);
	}
	
	public Angle negate() {
		return new Angle(-this.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Angle abs() {
		return new Angle(Math.abs(this.get(Unit.defaultUnit)), Unit.defaultUnit);
	}
	
	@Override
	public boolean equals(Object otherAngle) {
		if(otherAngle instanceof Angle) {
			return this.get(Unit.defaultUnit) == ((Angle) otherAngle).get(Unit.defaultUnit);
		} else {
			return false;
		}
	}

}
