package edu.nr.lib.units;

public class AngularSpeed {
	
	public static final AngularSpeed ZERO = new AngularSpeed(0, Unit.RPS);
	private double val;
	private Unit type;
	
	public enum Unit {
		DEGREEPERSECOND, RPM, RPS;
		
		private static Unit defaultUnit = RPS;
		
		private static final double DEGREEPERSECOND_PER_RPS = 1/360.0;
		private static final double RPM_PER_RPS = 60;
				
		static public double convertToDefault(double val, Unit fromType) {
			if(fromType == Unit.RPS) {
				return val;
			}
			if(fromType == Unit.RPM) {
				return val / RPM_PER_RPS;
			}
			if(fromType == Unit.DEGREEPERSECOND) {
				return val / DEGREEPERSECOND_PER_RPS;
			}
			return 0;
		}
		
		static public double convertFromDefault(double val, Unit toType) {
			if(toType == Unit.RPS) {
				return val;
			}
			if(toType == Unit.RPM) {
				return RPM_PER_RPS * val;
			}
			if(toType == Unit.DEGREEPERSECOND) {
				return DEGREEPERSECOND_PER_RPS * val;
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
	
	public AngularSpeed(double val, Unit type) {
		this.val = val;
		this.type = type;
	}
	
	public double get(Unit toType) {
		return Unit.convert(val, type, toType);
	}
	
	public AngularSpeed sub(AngularSpeed angleTwo) {
		return new AngularSpeed(this.get(Unit.defaultUnit) - angleTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public AngularSpeed add(AngularSpeed angleTwo) {
		return new AngularSpeed(this.get(Unit.defaultUnit) + angleTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public boolean lessThan(AngularSpeed angleTwo) {
		return this.get(Unit.defaultUnit) < angleTwo.get(Unit.defaultUnit);
	}

	public boolean greaterThan(AngularSpeed angleTwo) {
		return this.get(Unit.defaultUnit) > angleTwo.get(Unit.defaultUnit);
	}
	
	public AngularSpeed negate() {
		return new AngularSpeed(-this.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public AngularSpeed abs() {
		return new AngularSpeed(Math.abs(this.get(Unit.defaultUnit)), Unit.defaultUnit);
	}

}
