package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.interfaces.DoublePIDOutput;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfiler;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfilerTwoMotor;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectoryPremade;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectorySimple;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.Drive.Gear;

public class DriveForwardCommand extends NRCommand {

	OneDimensionalMotionProfiler profiler;
	double distance;
	
	//These are the one-dimensional motion profiling values
	public static final double KA = 0;
	public static final double KP = 0;
	public static final double KV = 0;
	public static final double KD = 0;
	public static final double KP_THETA = 0;
	public static final long period = 0;
	public static final double MAX_SPEED_PERCENTAGE = 0;
	
	public DriveForwardCommand(double inches) {
		super(Drive.getInstance());
		this.distance = inches / (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI);
	}

	@Override
	public void onStart() {
		profiler = new OneDimensionalMotionProfilerTwoMotor(Drive.getInstance(), Drive.getInstance(), KV, KA, KP, KD, KP_THETA, period);
		if (Drive.getInstance().getCurrentGear() == Gear.low) {
			profiler.setTrajectory(new OneDimensionalTrajectorySimple(distance , RobotMap.MAX_DRIVE_LOW_GEAR_SPEED / RobotMap.INCHES_PER_FOOT / (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI), RobotMap.MAX_DRIVE_LOW_GEAR_SPEED / RobotMap.INCHES_PER_FOOT / (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI) * MAX_SPEED_PERCENTAGE, RobotMap.MAX_DRIVE_ACCELERATION / RobotMap.INCHES_PER_FOOT / (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI)));
		} else {
			profiler.setTrajectory(new OneDimensionalTrajectorySimple(distance, RobotMap.MAX_DRIVE_HIGH_GEAR_SPEED / RobotMap.INCHES_PER_FOOT / (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI), RobotMap.MAX_DRIVE_HIGH_GEAR_SPEED / RobotMap.INCHES_PER_FOOT / (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI) * MAX_SPEED_PERCENTAGE, RobotMap.MAX_DRIVE_ACCELERATION / RobotMap.INCHES_PER_FOOT / (RobotMap.DRIVE_WHEEL_DIAMETER * Math.PI)));
			profiler.enable();
		}
	}
}
