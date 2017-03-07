package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfiler;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfilerTwoMotor;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectorySimple;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.subsystems.drive.Drive.Gear;

public class DriveForwardProfilingCommand extends NRCommand {

	OneDimensionalMotionProfiler profiler;
	Distance distance; // Rotations

	// These are the one-dimensional motion profiling values
	// TODO: DriveForwardProfilingCommand: Find the correct constants for one-dimensional
	// motion profiling
	public static final double KA = 0;
	public static final double KP = 0;
	public static double KV = 1 / (Drive.MAX_LOW_GEAR_SPEED.mul(Drive.WHEEL_DIAMETER.mul(Math.PI).get(Distance.Unit.METER))).get(Distance.Unit.METER, Time.Unit.SECOND);
	public static final double KD = 0;
	public static final double KP_THETA = 0;
	public static final double MAX_SPEED_PERCENTAGE = 0.75;

	/**
	 * Drive forward
	 * 
	 * @param distance
	 */
	public DriveForwardProfilingCommand(Distance distance) {
		super(Drive.getInstance());
		this.distance = distance;
	}

	@Override
	public void onStart() {
		if (Drive.getInstance().getCurrentGear() == Gear.high) {
			Drive.getInstance().switchGear();
		}
		profiler = new OneDimensionalMotionProfilerTwoMotor(Drive.getInstance(), Drive.getInstance(), KV, KA, KP, KD,
				KP_THETA);
		if (Drive.getInstance().getCurrentGear() == Gear.low) {
			profiler.setTrajectory(new OneDimensionalTrajectorySimple(distance.get(Distance.Unit.DRIVE_ROTATION),
					Drive.MAX_LOW_GEAR_SPEED.get(Distance.Unit.METER, Time.Unit.SECOND),
					Drive.MAX_LOW_GEAR_SPEED.get(Distance.Unit.METER, Time.Unit.SECOND) * MAX_SPEED_PERCENTAGE,
					Drive.MAX_ACCELERATION.get(Distance.Unit.METER, Time.Unit.SECOND, Time.Unit.SECOND)));	
		} else {
			profiler.setTrajectory(new OneDimensionalTrajectorySimple(distance.get(Distance.Unit.DRIVE_ROTATION),
					Drive.MAX_HIGH_GEAR_SPEED.get(Distance.Unit.METER, Time.Unit.SECOND),
					Drive.MAX_HIGH_GEAR_SPEED.get(Distance.Unit.METER, Time.Unit.SECOND) * MAX_SPEED_PERCENTAGE,
					Drive.MAX_ACCELERATION.get(Distance.Unit.METER, Time.Unit.SECOND, Time.Unit.SECOND)));
		}
		profiler.enable();
	}

	@Override
	public void onEnd() {
		profiler.disable();
	}
	
	@Override
	public boolean isFinishedNR() {
		boolean finished = false;
		//boolean finished = Drive.getInstance().getHistoricalLeftPosition(Drive.PROFILE_TIME_THRESHOLD).abs().sub(Drive.getInstance().getLeftPosition())
		//		.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
		//		&& Drive.getInstance().getHistoricalLeftPosition(Drive.PROFILE_TIME_THRESHOLD.mul(2)).abs().sub(Drive.getInstance().getLeftPosition())
		//		.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
		//		&& Drive.getInstance().getHistoricalRightPosition(Drive.PROFILE_TIME_THRESHOLD).abs().sub(Drive.getInstance().getRightPosition())
		//		.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
		//		&& Drive.getInstance().getHistoricalRightPosition(Drive.PROFILE_TIME_THRESHOLD.mul(2)).abs().sub(Drive.getInstance().getRightPosition())
		//		.lessThan(Drive.PROFILE_POSITION_THRESHOLD);
		System.out.println(finished);
		return finished;
	}
}
