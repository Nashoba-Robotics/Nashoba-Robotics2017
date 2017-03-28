package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfiler;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfilerTwoMotor;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectorySimple;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.subsystems.drive.Drive.Gear;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class DriveForwardProfilingExtendableCommand extends NRCommand {

	OneDimensionalMotionProfiler profiler;
	Distance startPosition;
	Distance distance;
	double speed;

	// These are the one-dimensional motion profiling values
	// TODO: DriveForwardProfilingCommand: Find the correct constants for one-dimensional
	// motion profiling
	public static double KA = 0;
	public static double KP = 1.2;
	public static double KV = 1 / Drive.MAX_LOW_GEAR_SPEED.get(Distance.Unit.DRIVE_ROTATION, Time.Unit.SECOND);
	public static double KD = 0;
	public static double KP_THETA = 0;//0.02;
	public static double MAX_SPEED_PERCENTAGE = 0.25;

	public abstract Distance distanceToGo();

	/**
	 * Drive forward
	 * @param speed 
	 */
	public DriveForwardProfilingExtendableCommand(double speed) {
		super(Drive.getInstance());
		this.speed = speed;
	}
	
	/**
	 * Drive forward
	 * @param speed 
	 */
	public DriveForwardProfilingExtendableCommand() {
		this(MAX_SPEED_PERCENTAGE);
	}
	
	@Override
	public void onStart() {
		Drive.getInstance().switchToLowGear();
		distance = distanceToGo();
		/*SmartDashboard.putNumber("KA", SmartDashboard.getNumber("KA", KA));
		SmartDashboard.putNumber("KP", SmartDashboard.getNumber("KP", KP));
		SmartDashboard.putNumber("KD", SmartDashboard.getNumber("KD", KD));
		SmartDashboard.putNumber("KP_THETA", SmartDashboard.getNumber("KP_THETA", KP_THETA));
		SmartDashboard.putNumber("Max Percentage", SmartDashboard.getNumber("Max Percentage", MAX_SPEED_PERCENTAGE));
		SmartDashboard.putNumber("Distance", SmartDashboard.getNumber("Distance", 0));
		
		KA = SmartDashboard.getNumber("KA", KA);
		KP = SmartDashboard.getNumber("KP", KP);
		KD = SmartDashboard.getNumber("KD", KD);
		KP_THETA = SmartDashboard.getNumber("KP_THETA", KP_THETA);
		MAX_SPEED_PERCENTAGE = SmartDashboard.getNumber("Max Percentage", MAX_SPEED_PERCENTAGE);
		this.distance = new Distance(SmartDashboard.getNumber("Distance", 0), Distance.Unit.INCH);
		
		KV = 1 / Drive.getInstance().currentMaxSpeed().get(Distance.Unit.DRIVE_ROTATION, Time.Unit.SECOND);*/
		
		startPosition = Drive.getInstance().getLeftPosition();
		
		profiler = new OneDimensionalMotionProfilerTwoMotor(Drive.getInstance(), Drive.getInstance(), KV, KA, KP, KD,
				KP_THETA);
		if (Drive.getInstance().getCurrentGear() == Gear.low) {
			profiler.setTrajectory(new OneDimensionalTrajectorySimple(distance.get(Distance.Unit.DRIVE_ROTATION),
					Drive.MAX_LOW_GEAR_SPEED.get(Distance.Unit.DRIVE_ROTATION, Time.Unit.SECOND),
					Drive.MAX_LOW_GEAR_SPEED.get(Distance.Unit.DRIVE_ROTATION, Time.Unit.SECOND) * speed,
					Drive.MAX_ACCELERATION.mul(0.75).get(Distance.Unit.DRIVE_ROTATION, Time.Unit.SECOND, Time.Unit.SECOND)));	
		} else {
			profiler.setTrajectory(new OneDimensionalTrajectorySimple(distance.get(Distance.Unit.DRIVE_ROTATION),
					Drive.MAX_HIGH_GEAR_SPEED.get(Distance.Unit.DRIVE_ROTATION, Time.Unit.SECOND),
					Drive.MAX_HIGH_GEAR_SPEED.get(Distance.Unit.DRIVE_ROTATION, Time.Unit.SECOND) * speed,
					Drive.MAX_ACCELERATION.mul(0.75).get(Distance.Unit.DRIVE_ROTATION, Time.Unit.SECOND, Time.Unit.SECOND)));
		}
		profiler.enable();
	}

	@Override
	public void onEnd() {
		profiler.disable();
	}
	
	@Override
	public boolean isFinishedNR() {
		boolean finished = (Drive.getInstance().getHistoricalLeftPosition(Drive.PROFILE_TIME_THRESHOLD).sub(Drive.getInstance().getLeftPosition())).abs()
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
				&& (Drive.getInstance().getHistoricalLeftPosition(Drive.PROFILE_TIME_THRESHOLD.mul(2)).sub(Drive.getInstance().getLeftPosition())).abs()
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
				&& (Drive.getInstance().getHistoricalRightPosition(Drive.PROFILE_TIME_THRESHOLD).sub(Drive.getInstance().getRightPosition())).abs()
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
				&& (Drive.getInstance().getHistoricalRightPosition(Drive.PROFILE_TIME_THRESHOLD.mul(2)).sub(Drive.getInstance().getRightPosition())).abs()
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
				&& (Drive.getInstance().getLeftPosition().sub(startPosition.add(distance))).abs().lessThan(Drive.PROFILE_POSITION_THRESHOLD);
		return finished;
	}
}
