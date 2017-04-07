package edu.nr.lib.units;

import edu.nr.lib.Units;

public class Angle {
	
	public static final Angle ZERO = new Angle(0, Unit.DEGREE);
	private double val;
	private Unit type;
	
	public enum Unit implements GenericUnit {
		DEGREE, ROTATION, RADIAN, MAGNETIC_ENCODER_NATIVE_UNITS;
		
		public static final Unit defaultUnit = DEGREE;
		
		private static final double ROTATIONS_PER_DEGREE = 1/360.0;
		private static final double MAGNETIC_ENCODER_NATIVE_UNITS_PER_ROTATION = Units.MAGNETIC_NATIVE_UNITS_PER_REV;
		private static final double MAGNETIC_ENCODER_NATIVE_UNITS_PER_DEGREE = MAGNETIC_ENCODER_NATIVE_UNITS_PER_ROTATION * ROTATIONS_PER_DEGREE; 
		private static final double RADIANS_PER_DEGREE = 2*Math.PI / 360.0;
				
		public double convertToDefault(double val) {
			if(this == Unit.DEGREE) {
				return val;
			}
			if(this == Unit.ROTATION) {
				return val / ROTATIONS_PER_DEGREE;
			}
			if(this == Unit.RADIAN) {
				return val / RADIANS_PER_DEGREE;
			}
			if(this == MAGNETIC_ENCODER_NATIVE_UNITS) {
				return val / MAGNETIC_ENCODER_NATIVE_UNITS_PER_DEGREE;
			}
			return 0;
		}
		
		public double convertFromDefault(double val) {
			if(this == Unit.DEGREE) {
				return val;
			}
			if(this == Unit.ROTATION) {
				return ROTATIONS_PER_DEGREE * val;
			}
			if(this == Unit.RADIAN) {
				return RADIANS_PER_DEGREE * val;
			}
			if(this == MAGNETIC_ENCODER_NATIVE_UNITS) {
				return val * MAGNETIC_ENCODER_NATIVE_UNITS_PER_DEGREE;
			}
			return 0;
		}
}
	
	public Angle(double val, Unit type) {
		this.val = val;
		this.type = type;
	}
	
	public double get(Unit toType) {
		return GenericUnit.convert(val, type, toType);
	}

	public double getDefault() {
		return get(Unit.defaultUnit);
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
	
	public double div(Angle angleTwo) {
		return this.get(Unit.defaultUnit) / angleTwo.get(Unit.defaultUnit);
	}
	
	public double cos() {
		return Math.cos(this.get(Unit.RADIAN));
	}
	
	public double sin() {
		return Math.sin(this.get(Unit.RADIAN));
	}
	
	public double tan() {
		return Math.tan(this.get(Unit.RADIAN));
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
	
	public double signum() {
		return Math.signum(this.get(Unit.defaultUnit));
	}
	
	@Override
	public boolean equals(Object angleTwo) {
		if(angleTwo instanceof Angle) {
			return this.get(Unit.defaultUnit) == ((Angle) angleTwo).get(Unit.defaultUnit);
		} else {
			return false;
		}
	}

}
