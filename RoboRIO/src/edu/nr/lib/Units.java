package edu.nr.lib;

import edu.nr.lib.units.Angle;

public class Units {

	/**
	 * The number of seconds per minute. This is used to convert from feet per second to rpm.
	 */
	public static final double SECONDS_PER_MINUTE = 60;
	/**
	 * The number of milliseconds per second. Used when comparing time stamps
	 */
	public static final double MILLISECONDS_PER_SECOND = 1000;
	/**
	 * The number of inches per foot.
	 */
	public static final double INCHES_PER_FOOT = 12;
	/**
	 * The number of inches in a meter. Used for two-dimensional motion profiling
	 */
	public static final double INCHES_PER_METER = 39.37;
	/**
	 * The number of degrees per rotation. This is used in move calculations to change rotations to degrees
	 */
	public static final double DEGREES_PER_ROTATION = 360;
	/**
	 * Number of degrees in a right angle
	 */
	public static final Angle RIGHT_ANGLE = new Angle(90, Angle.Type.DEGREE);
	public static final int MAGNETIC_NATIVE_UNITS_PER_REV = 4096;
	public static final double HUNDRED_MS_PER_SECOND = 10;
	/**
	 * The number of hundred of millisecond periods per minute
	 * 
	 * This is used to calculate the feedforward value for a velocity feedback PID that is running on a CANTalon.
	 */
	public static final double HUNDRED_MS_PER_MIN = HUNDRED_MS_PER_SECOND * SECONDS_PER_MINUTE;


}
