package edu.nr.robotics;

import edu.nr.lib.Units;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.auton.AutoTravelMethod;
import edu.nr.robotics.auton.GearAlignMethod;
import edu.nr.robotics.auton.HopperRamStopMethod;
import edu.nr.robotics.auton.ShootAlignMode;

public class FieldMap {
	
	/**
	 * The way we get around in autonomous mode
	 */
	public static final AutoTravelMethod autoTravelMethod = AutoTravelMethod.OneDProfilingAndPID;
	
	/**
	 * The way we stop ramming into the hopper (time or current)
	 */
	public static final HopperRamStopMethod hopperRamStopMethod = HopperRamStopMethod.current;
	
	/**
	 * The way we get to the gear peg (camera or profiling)
	 */
	public static final GearAlignMethod gearAlignMethod = GearAlignMethod.camera;
	
	public static final ShootAlignMode shootAlignMode = ShootAlignMode.manual;
	
	/**
	 * The autonomous distance from the wall to the center peg in inches.
	 * The distance is the distance the robot needs to drive and not the true distance
	 */
	public static final Distance DISTANCE_TO_CENTER_PEG = new Distance(114.3, Distance.Unit.INCH);
	
	/**
	 * The distance we want to drive into the peg once we have reached it
	 */
	public static final Distance DRIVE_DEPTH_ON_PEG_FROM_SHIP = new Distance(0.5, Distance.Unit.INCH);
	
	/**
	 * The dimensions to the pegs in auto that would be used in inches or degrees
	 * The side distances assume that robot is on line
	 * 
	 * TODO: Field: Find the side distances to the side pegs that would be used
	 */
	public static final Distance FORWARD_DISTANCE_TO_SIDE_PEG = new Distance(132.05, Distance.Unit.INCH);
	public static final Distance SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG = Distance.ZERO;
	public static final Distance SIDE_DISTANCE_TO_NON_SHOOTER_SIDE_PEG = Distance.ZERO;
	public static final Angle ANGLE_TO_SIDE_PEG = new Angle(60, Angle.Unit.DEGREE);
	
	/**
	 * The dimensions to the hopper in auto that would be used in inches or degrees
	 * The side distance assumes that robot center is on line and that center will hit edge of hopper
	 */
	public static final Distance FORWARD_DISTANCE_WALL_TO_HOPPER = new Distance(103.2, Distance.Unit.INCH);
	public static final Distance SIDE_DISTANCE_WALL_TO_HOPPER = new Distance(103.2, Distance.Unit.INCH);
	public static final Angle ANGLE_WALL_TO_HOPPER = Units.RIGHT_ANGLE;
	
	/**
	 * The distances the robot would need to go from the gear to the hopper in inches
	 * 
	 *  TODO: Field: Find distance robot needs to drive from gear to hopper
	 */
	public static final Distance GEAR_TO_HOPPER_SIDE_DIST = Distance.ZERO;
	
	/**
	 * The amount of time to wait without a picture before sweeping
	 */
	public static final Time MIN_TRACKING_WAIT_TIME = new Time(0.5, Time.Unit.SECOND);
	
	/**
	 * Distance away from hopper to stop before ramming into it
	 */
	public final static Distance STOP_DISTANCE_FROM_HOPPER = new Distance(6, Distance.Unit.INCH);

	/**
	 * Distance away from peg (stop point) to stop before ramming into it
	 */
	public final static Distance GEAR_ALIGN_STOP_DISTANCE_FROM_PEG = new Distance(1, Distance.Unit.METER);
}
