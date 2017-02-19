package edu.nr.robotics;

import edu.nr.lib.units.Angle;

public class FieldMap {

	/**
	 * The autonomous distance from the wall to the center peg in inches.
	 * The distance is the distance the robot needs to drive and not the true distance
	 * 
	 * TODO: Field: Get the distance from the robot to the center peg in auto
	 */
	public static final double DISTANCE_TO_CENTER_PEG = 0;
	/**
	 * The dimensions to the pegs in auto that would be used in inches or degrees
	 * The angle to the side peg assumes
	 * 
	 * TODO: Field: Find the distaces to the side pegs that would be used
	 */
	public static final double FORWARD_DISTANCE_TO_SIDE_PEG = 0;
	public static final double SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG = 0;
	public static final double SIDE_DISTANCE_TO_NON_SHOOTER_SIDE_PEG = 0;
	public static final Angle ANGLE_TO_SIDE_PEG = new Angle(45, Angle.Unit.DEGREE);
	/**
	 * The dimensions to the hopper in auto that would be used in inches or degrees
	 * The angle to the hopper is assuming hopper is on left and positive is right
	 * 
	 * TODO: Field: Find the distances to the hoppers that would be used
	 */
	public static final double FORWARD_DISTANCE_WALL_TO_HOPPER = 0;
	public static final double SIDE_DISTANCE_WALL_TO_HOPPER = 0;
	public static final Angle ANGLE_WALL_TO_HOPPER = Angle.ZERO;
	/**
	 * The distances the robot would need to go from the gear to the hopper in inches
	 * 
	 *  TODO: Field: Find distance robot needs to drive from gear to hopper
	 */
	public static final double GEAR_TO_HOPPER_SIDE_DIST = 0;
	public static final double GEAR_TO_HOPPER_FORWARD_DIST = 0;

}
