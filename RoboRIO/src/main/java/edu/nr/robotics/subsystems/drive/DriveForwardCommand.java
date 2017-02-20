package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfiler;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfilerTwoMotor;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectorySimple;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.subsystems.drive.Drive.Gear;

public class DriveForwardCommand extends NRCommand {

	OneDimensionalMotionProfiler profiler;
	Distance distance; // Rotations

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
	 * 
	 * @param distance
	 */
	public DriveForwardCommand(Distance distance) {
		super(Drive.getInstance());
		this.distance = distance;
	}

	@Override
	public void onStart() {
		profiler = new OneDimensionalMotionProfilerTwoMotor(Drive.getInstance(), Drive.getInstance(), KV, KA, KP, KD,
				KP_THETA);
		if (Drive.getInstance().getCurrentGear() == Gear.low) {
			profiler.setTrajectory(new OneDimensionalTrajectorySimple(distance.get(Distance.Unit.DRIVE_ROTATION),
					Drive.MAX_LOW_GEAR_SPEED * Units.INCHES_PER_FOOT / Drive.DISTANCE_PER_REV_INCHES,
					Drive.MAX_LOW_GEAR_SPEED * Units.INCHES_PER_FOOT / Drive.DISTANCE_PER_REV_INCHES * MAX_SPEED_PERCENTAGE,
					Drive.MAX_ACCELERATION * Units.INCHES_PER_FOOT / Drive.DISTANCE_PER_REV_INCHES));
		} else {
			profiler.setTrajectory(new OneDimensionalTrajectorySimple(distance.get(Distance.Unit.DRIVE_ROTATION),
					Drive.MAX_HIGH_GEAR_SPEED * Units.INCHES_PER_FOOT / Drive.DISTANCE_PER_REV_INCHES,
					Drive.MAX_HIGH_GEAR_SPEED * Units.INCHES_PER_FOOT / Drive.DISTANCE_PER_REV_INCHES * MAX_SPEED_PERCENTAGE,
					Drive.MAX_ACCELERATION * Units.INCHES_PER_FOOT / Drive.DISTANCE_PER_REV_INCHES));
		}
		profiler.enable();
	}

	@Override
	public boolean isFinishedNR() {
		return Drive.getInstance().getHistoricalLeftPosition(Drive.PROFILE_TIME_THRESHOLD).abs().sub(Drive.getInstance().getLeftPosition())
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
				&& Drive.getInstance().getHistoricalLeftPosition(Drive.PROFILE_TIME_THRESHOLD.mul(2)).abs().sub(Drive.getInstance().getLeftPosition())
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
				&& Drive.getInstance().getHistoricalRightPosition(Drive.PROFILE_TIME_THRESHOLD).abs().sub(Drive.getInstance().getRightPosition())
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
				&& Drive.getInstance().getHistoricalRightPosition(Drive.PROFILE_TIME_THRESHOLD.mul(2)).abs().sub(Drive.getInstance().getRightPosition())
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD);
	}
}
