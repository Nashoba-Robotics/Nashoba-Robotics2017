package edu.nr.lib.units;

public interface GenericUnit {

	abstract double convertToDefault(double val);
	abstract double convertFromDefault(double val);
	


	static public double convert(double val, GenericUnit fromType, GenericUnit toType) {
		if(fromType == toType) {
			return val;
		}
		return toType.convertFromDefault(fromType.convertToDefault(val));
	}
	
	public default double convert(double val, GenericUnit toType) {
		if(this == toType) {
			return val;
		}
		return toType.convertFromDefault(this.convertToDefault(val));
	}
}