package edu.nr.lib;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;

public class NRMath {

	/**
	 * Squares the input value and maintains the sign
	 * 
	 * @param x
	 * @return the squared value with the sign maintained
	 */
	public static double squareWithSign(double x) {
		return powWithSign(x, 2);
	}

	/**
	 * Returns the value of the first argument raised to the power of the second
	 * argument multiplied by the sign of the first argument
	 * 
	 * @param x
	 *            the base
	 * @param exp
	 *            the exponent
	 * @return the value a^b * the sign of a
	 */
	public static double powWithSign(double x, double exp) {
		return Math.pow(x, exp) * Math.signum(x);
	}

	/**
	 * Limits x from -y to y
	 * 
	 * @param x
	 * @param y
	 * @return the value, limited from -y to y
	 */
	public static double limit(double x, double y) {
		if (x > y) {
			return y;
		}
		if (x < -y) {
			return -y;
		}
		return x;
	}

	/**
	 * Limits x from -1 to 1
	 * 
	 * @param x
	 * @return the value, limited between -1 and 1
	 */
	public static double limit(double x) {
		return limit(x, 1);
	}

	public static Angle asin(double a) {
		return new Angle(Math.asin(a), Angle.Unit.RADIAN);
	}

	/*
	 * public static Angle atan(double a) { return new Angle(Math.atan(a),
	 * Angle.Type.RADIAN); }
	 */

	public static Angle atan2(double a, double b) {
		return new Angle(Math.atan2(a, b), Angle.Unit.RADIAN);
	}

	public static Angle acos(double a) {
		return new Angle(Math.acos(a), Angle.Unit.RADIAN);
	}

	public static double lawOfCos(double a, double b, Angle C) {
		return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2) + 2 * a * b * C.cos());
	}

	public static Speed average(Speed leftSpeed, Speed rightSpeed) {
		return new Speed((leftSpeed.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit)
				+ rightSpeed.get(Distance.Unit.defaultUnit, Time.Unit.defaultUnit))/2, Distance.Unit.defaultUnit, Time.Unit.defaultUnit);
	}
}
