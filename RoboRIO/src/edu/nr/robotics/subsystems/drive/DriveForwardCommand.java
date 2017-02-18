package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfiler;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfilerTwoMotor;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectorySimple;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.Drive.Gear;

public class DriveForwardCommand extends NRCommand {

	OneDimensionalMotionProfiler profiler;
	double distance; //Rotations

	// These are the one-dimensional motion profiling values
	// TODO: DriveForwardCommand: Find the correct constants for one-dimensional
	// motion profiling
	public static final double KA = 0;
	public static final double KP = 0;
	public static final double KV = 0;
	public static final double KD = 0;
	public static final double KP_THETA = 0;
	public static final double MAX_SPEED_PERCENTAGE = 0;

	/**
	 * Drive forward
	 * @param distance The distance in inches
	 */
	public DriveForwardCommand(double distance) {
		super(Drive.getInstance());
		this.distance = distance / (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI);
	}

	@Override
	public void onStart() {
		profiler = new OneDimensionalMotionProfilerTwoMotor(Drive.getInstance(), Drive.getInstance(), KV, KA, KP, KD, KP_THETA);
		if (Drive.getInstance().getCurrentGear() == Gear.low) {
			profiler.setTrajectory(new OneDimensionalTrajectorySimple(distance,
					RobotMap.MAX_DRIVE_LOW_GEAR_SPEED * RobotMap.INCHES_PER_FOOT
							/ (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI),
					RobotMap.MAX_DRIVE_LOW_GEAR_SPEED * RobotMap.INCHES_PER_FOOT
							/ (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI) * MAX_SPEED_PERCENTAGE,
					RobotMap.MAX_DRIVE_ACCELERATION * RobotMap.INCHES_PER_FOOT
							/ (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI)));
		} else {
			profiler.setTrajectory(new OneDimensionalTrajectorySimple(distance,
					RobotMap.MAX_DRIVE_HIGH_GEAR_SPEED * RobotMap.INCHES_PER_FOOT
							/ (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI),
					RobotMap.MAX_DRIVE_HIGH_GEAR_SPEED * RobotMap.INCHES_PER_FOOT
							/ (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI) * MAX_SPEED_PERCENTAGE,
					RobotMap.MAX_DRIVE_ACCELERATION * RobotMap.INCHES_PER_FOOT
							/ (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI)));
		}
		profiler.enable();
	}

	@Override
	public boolean isFinishedNR() {
		if (Math.abs(Drive.getInstance().getHistoricalLeftPosition((long) RobotMap.PROFILE_TIME_THRESHOLD)
				- Drive.getInstance().getLeftPosition()) < RobotMap.PROFILE_POSITION_THRESHOLD
				&& Math.abs(Drive.getInstance().getHistoricalLeftPosition((long) RobotMap.PROFILE_TIME_THRESHOLD * 2)
						- Drive.getInstance().getLeftPosition()) < RobotMap.PROFILE_POSITION_THRESHOLD
				&& Math.abs(Drive.getInstance().getHistoricalRightPosition((long) RobotMap.PROFILE_TIME_THRESHOLD)
						- Drive.getInstance().getRightPosition()) < RobotMap.PROFILE_POSITION_THRESHOLD
				&& Math.abs(Drive.getInstance().getHistoricalRightPosition((long) RobotMap.PROFILE_TIME_THRESHOLD * 2)
						- Drive.getInstance().getRightPosition()) < RobotMap.PROFILE_POSITION_THRESHOLD)
			return true;
		return false;
	}
}
