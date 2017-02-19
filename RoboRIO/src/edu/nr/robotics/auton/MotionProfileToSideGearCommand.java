package edu.nr.robotics.auton;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.motionprofiling.TwoDimensionalMotionProfilerPathfinder;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Type;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.Drive.Gear;
import jaci.pathfinder.Waypoint;

public class MotionProfileToSideGearCommand extends NRCommand {

	// TODO: MotionProfileToSideGearCommand: Make this read files instead of
	// motion profile

	TwoDimensionalMotionProfilerPathfinder profiler;
	double forwardDistance; // In meters down below, inches on input
	double sideDistance; // In meters down below, inches on input
	Angle endHeading;

	// Two-Dimensional motion profiling constants
	// TODO: MotionProfileToSideGearCommand: Get two-dimensional motion
	// profiling constants
	public static final double KA = 0;
	public static final double KP = 0;
	public static final double KV = 0;
	public static final double KI = 0;
	public static final double KD = 0;
	public static final double KP_THETA = 0;
	public static final double MAX_SPEED_PERCENTAGE = 0;
	public static final double DISTANCE_FROM_ENDPOINT = 0; // On the path the
															// distance away
															// from the path
															// endpoint that we
															// want to stay
															// straight for

	public MotionProfileToSideGearCommand(double forwardDistance, double sideDistance, Angle endHeading) {
		super(Drive.getInstance());
		this.forwardDistance = forwardDistance / Units.INCHES_PER_METER;
		this.sideDistance = sideDistance / Units.INCHES_PER_METER;
		this.endHeading = endHeading;
	}

	@Override
	public void onStart() {
		if (Drive.getInstance().getCurrentGear() == Gear.low) {
			profiler = new TwoDimensionalMotionProfilerPathfinder(Drive.getInstance(), Drive.getInstance(), KV, KA, KP,
					KI, KD, KP_THETA,
					Drive.MAX_LOW_GEAR_SPEED * Units.INCHES_PER_FOOT / Units.INCHES_PER_METER
							* MAX_SPEED_PERCENTAGE,
					Drive.MAX_ACCELERATION * Units.INCHES_PER_FOOT / Units.INCHES_PER_METER
							* MAX_SPEED_PERCENTAGE,
					Drive.MAX_JERK * Units.INCHES_PER_FOOT / Units.INCHES_PER_METER,
					Drive.TICKS_PER_REV, Drive.WHEEL_DIAMETER / Units.INCHES_PER_METER, Drive.WHEEL_BASE);
		} else {
			profiler = new TwoDimensionalMotionProfilerPathfinder(Drive.getInstance(), Drive.getInstance(), KV, KA, KP,
					KI, KD, KP_THETA,
					Drive.MAX_HIGH_GEAR_SPEED * Units.INCHES_PER_FOOT / Units.INCHES_PER_METER
							* MAX_SPEED_PERCENTAGE,
					Drive.MAX_ACCELERATION * Units.INCHES_PER_FOOT / Units.INCHES_PER_METER
							* MAX_SPEED_PERCENTAGE,
					Drive.MAX_JERK * Units.INCHES_PER_FOOT / Units.INCHES_PER_METER,
					Drive.TICKS_PER_REV, Drive.WHEEL_DIAMETER / Units.INCHES_PER_METER, Drive.WHEEL_BASE);
		}
		profiler.setTrajectory(new Waypoint[] { new Waypoint(0, 0, 0),
				new Waypoint(forwardDistance - DISTANCE_FROM_ENDPOINT * endHeading.cos(),
						sideDistance - DISTANCE_FROM_ENDPOINT * endHeading.sin(), endHeading.get(Type.RADIAN)),
				new Waypoint(forwardDistance, sideDistance, endHeading.get(Type.RADIAN)) });
		profiler.enable();
	}

	@Override
	public void onEnd() {
		profiler.disable();
	}

	@Override
	public boolean isFinishedNR() {
		if (Math.abs(Drive.getInstance().getHistoricalLeftPosition((long) Drive.PROFILE_TIME_THRESHOLD)
				- Drive.getInstance().getLeftPosition()) < Drive.PROFILE_POSITION_THRESHOLD
				&& Math.abs(Drive.getInstance().getHistoricalLeftPosition((long) Drive.PROFILE_TIME_THRESHOLD * 2)
						- Drive.getInstance().getLeftPosition()) < Drive.PROFILE_POSITION_THRESHOLD
				&& Math.abs(Drive.getInstance().getHistoricalRightPosition((long) Drive.PROFILE_TIME_THRESHOLD)
						- Drive.getInstance().getRightPosition()) < Drive.PROFILE_POSITION_THRESHOLD
				&& Math.abs(Drive.getInstance().getHistoricalRightPosition((long) Drive.PROFILE_TIME_THRESHOLD * 2)
						- Drive.getInstance().getRightPosition()) < Drive.PROFILE_POSITION_THRESHOLD)
			return true;
		return false;
	}

}
