package edu.nr.lib.units;

public class Angle {
	
	public static final Angle ZERO = new Angle(0, Type.DEGREE);
	private double val;
	private Type type;
	
	public enum Type {
		DEGREE, ROTATION, RADIAN;
		
		private static final double ROTATIONS_PER_DEGREE = 1/360.0;
		private static final double RADIANS_PER_DEGREE = 2*Math.PI / 360.0;
				
		static public double convertToDefault(double val, Type fromType) {
			if(fromType == Type.DEGREE) {
				return val;
			}
			if(fromType == Type.ROTATION) {
				return val / ROTATIONS_PER_DEGREE;
			}
			if(fromType == Type.RADIAN) {
				return val / RADIANS_PER_DEGREE;
			}
			return 0;
		}
		
		static public double convertFromDefault(double val, Type toType) {
			if(toType == Type.DEGREE) {
				return val;
			}
			if(toType == Type.ROTATION) {
				return ROTATIONS_PER_DEGREE * val;
			}
			if(toType == Type.RADIAN) {
				return RADIANS_PER_DEGREE * val;
			}
			return 0;
		}

		static public double convert(double val, Type fromType, Type toType) {
			if(fromType == toType) {
				return val;
			}
			return convertFromDefault(convertToDefault(val, fromType), toType);
		}
}
	
	public Angle(double val, Type type) {
		this.val = val;
		this.type = type;
	}
	
	public double get(Type toType) {
		return Type.convert(val, type, toType);
	}
	
	public Angle sub(Angle angleTwo) {
		return new Angle(this.get(Type.DEGREE) - angleTwo.get(Type.DEGREE), Type.DEGREE);
	}
	
	public Angle add(Angle angleTwo) {
		return new Angle(this.get(Type.DEGREE) + angleTwo.get(Type.DEGREE), Type.DEGREE);
	}
	
	public Angle mul(double x) {
		return new Angle(this.get(Type.DEGREE) * x, Type.DEGREE);
	}
	
	public double cos() {
		return Math.cos(this.get(Type.RADIAN));
	}
	
	public double sin() {
		return Math.sin(this.get(Type.RADIAN));
	}
	
	public boolean lessThan(Angle angleTwo) {
		return this.get(Type.DEGREE) < angleTwo.get(Type.DEGREE);
	}

	public boolean greaterThan(Angle angleTwo) {
		return this.get(Type.DEGREE) > angleTwo.get(Type.DEGREE);
	}
	
	public Angle negate() {
		return new Angle(-this.get(Type.DEGREE), Type.DEGREE);
	}
	
	public Angle abs() {
		return new Angle(Math.abs(this.get(Type.DEGREE)), Type.DEGREE);
	}

}
