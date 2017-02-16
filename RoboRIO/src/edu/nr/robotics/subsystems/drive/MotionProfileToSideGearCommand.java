package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.motionprofiling.TwoDimensionalMotionProfilerPathfinder;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.Drive.Gear;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class MotionProfileToSideGearCommand extends NRCommand {
	
	// TODO: MotionProfileToSideGearCommand: Make this read files instead of motion profile
	
	TwoDimensionalMotionProfilerPathfinder profiler;
	double forwardDistance; //In meters down below, inches on input
	double sideDistance; //In meters down below, inches on input
	double endHeading; // In degrees
	
	//Two-Dimensional motion profiling constants
	// TODO: MotionProfileToSideGearCommand: Get two-dimensional motion profiling constants
	public static final double KA = 0;
	public static final double KP = 0;
	public static final double KV = 0;
	public static final double KI = 0;
	public static final double KD = 0;
	public static final double KP_THETA = 0;
	public static final long period = 0; //Number of times per second to run
	public static final double MAX_SPEED_PERCENTAGE = 0;
	public static final double DISTANCE_FROM_ENDPOINT = 0; // On the path the distance away from the path endpoint that we want to stay straight for
	
	// TODO: MotionProfileToSideGearCommand: Get thresholds to finish motion profiling
	//time in milliseconds
	public static final long PROFILE_TIME_THRESHOLD = 0; // Delta time checked for to compare talon positions to previous positions to end profiler
	public static final double PROFILE_POSITION_THRESHOLD = 0; // Position difference compared to end profiler
	
	public MotionProfileToSideGearCommand(double forwardDistance, double sideDistance, double endHeading) {
		super(Drive.getInstance());
		this.forwardDistance = forwardDistance / RobotMap.INCHES_PER_METER;
		this.sideDistance = sideDistance / RobotMap.INCHES_PER_METER;
		this.endHeading = endHeading;
	}
	
	@Override
	public void onStart() {
		if (Drive.getInstance().getCurrentGear() == Gear.low) {
			profiler = new TwoDimensionalMotionProfilerPathfinder(Drive.getInstance(), Drive.getInstance(), KV, KA, KP, KI, KD, KP_THETA, RobotMap.MAX_DRIVE_LOW_GEAR_SPEED * RobotMap.INCHES_PER_FOOT / RobotMap.INCHES_PER_METER * MAX_SPEED_PERCENTAGE, RobotMap.MAX_DRIVE_ACCELERATION * RobotMap.INCHES_PER_FOOT / RobotMap.INCHES_PER_METER * MAX_SPEED_PERCENTAGE, RobotMap.MAX_DRIVE_ACCELERATION * RobotMap.INCHES_PER_FOOT / RobotMap.INCHES_PER_METER * MAX_SPEED_PERCENTAGE, Drive.TICKS_PER_REV, RobotMap.DRIVE_WHEEL_DIAMETER / RobotMap.INCHES_PER_METER, period);
		} else {
			profiler = new TwoDimensionalMotionProfilerPathfinder(Drive.getInstance(), Drive.getInstance(), KV, KA, KP, KI, KD, KP_THETA, RobotMap.MAX_DRIVE_HIGH_GEAR_SPEED * RobotMap.INCHES_PER_FOOT / RobotMap.INCHES_PER_METER * MAX_SPEED_PERCENTAGE, RobotMap.MAX_DRIVE_ACCELERATION * RobotMap.INCHES_PER_FOOT / RobotMap.INCHES_PER_METER * MAX_SPEED_PERCENTAGE, RobotMap.MAX_DRIVE_ACCELERATION * RobotMap.INCHES_PER_FOOT / RobotMap.INCHES_PER_METER * MAX_SPEED_PERCENTAGE, Drive.TICKS_PER_REV, RobotMap.DRIVE_WHEEL_DIAMETER / RobotMap.INCHES_PER_METER, period);			
		}
		profiler.setTrajectory(new Waypoint[] {
				new Waypoint(0, 0, 0),
				new Waypoint(forwardDistance - DISTANCE_FROM_ENDPOINT * Math.cos(endHeading), sideDistance - DISTANCE_FROM_ENDPOINT * Math.sin(endHeading), Pathfinder.d2r(endHeading)),
				new Waypoint(forwardDistance, sideDistance, Pathfinder.d2r(endHeading))
		});
		profiler.enable();
	}
	
	@Override
	public void onEnd() {
		profiler.disable();
	}
	
	@Override
	public boolean isFinishedNR() {
		if (Math.abs(Drive.getInstance().getHistoricalLeftPosition(PROFILE_TIME_THRESHOLD) - Drive.getInstance().getLeftPosition()) < PROFILE_POSITION_THRESHOLD && Math.abs(Drive.getInstance().getHistoricalLeftPosition(PROFILE_TIME_THRESHOLD * 2) - Drive.getInstance().getLeftPosition()) < PROFILE_POSITION_THRESHOLD && Math.abs(Drive.getInstance().getHistoricalRightPosition(PROFILE_TIME_THRESHOLD) - Drive.getInstance().getRightPosition()) < PROFILE_POSITION_THRESHOLD && Math.abs(Drive.getInstance().getHistoricalRightPosition(PROFILE_TIME_THRESHOLD * 2) - Drive.getInstance().getRightPosition()) < PROFILE_POSITION_THRESHOLD)
			return true;
		return false;
	}
	
}
