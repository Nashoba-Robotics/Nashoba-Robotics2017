package edu.nr.robotics.auton;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.motionprofiling.TwoDimensionalMotionProfilerPathfinder;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.Drive.Gear;
import jaci.pathfinder.Waypoint;

public class MotionProfileToSideGearCommand extends NRCommand {

	// TODO: MotionProfileToSideGearCommand: Make this read files instead of
	// motion profile

	TwoDimensionalMotionProfilerPathfinder profiler;
	Distance forwardDistance; // In meters down below, inches on input
	Distance sideDistance; // In meters down below, inches on input
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
	
	/**
	 * On the path the distance away from the path endpoint that we want to stay
	 * straight for
	 */
	public static final Distance DISTANCE_FROM_ENDPOINT = Distance.ZERO;

	public MotionProfileToSideGearCommand(Distance forwardDistance, Distance sideDistance, Angle endHeading) {
		super(Drive.getInstance());
		this.forwardDistance = forwardDistance;
		this.sideDistance = sideDistance;
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
		profiler.setTrajectory(new Waypoint[] { new Waypoint(0, 0, 0), new Waypoint(
				forwardDistance.sub((DISTANCE_FROM_ENDPOINT.mul(endHeading.cos()))).get(Distance.Unit.DRIVE_ROTATION),
				sideDistance.sub(DISTANCE_FROM_ENDPOINT.mul(endHeading.sin())).get(Distance.Unit.DRIVE_ROTATION),
				endHeading.get(Unit.RADIAN)),
				new Waypoint(forwardDistance.get(Distance.Unit.DRIVE_ROTATION),
						sideDistance.get(Distance.Unit.DRIVE_ROTATION), endHeading.get(Unit.RADIAN)) });
		profiler.enable();
	}

	@Override
	public void onEnd() {
		profiler.disable();
	}

	@Override
	public boolean isFinishedNR() {
		if (Math.abs(Drive.getInstance().getHistoricalLeftPosition(Drive.PROFILE_TIME_THRESHOLD)
				- Drive.getInstance().getLeftPosition()) < Drive.PROFILE_POSITION_THRESHOLD
				&& Math.abs(Drive.getInstance().getHistoricalLeftPosition(Drive.PROFILE_TIME_THRESHOLD.mul(2))
						- Drive.getInstance().getLeftPosition()) < Drive.PROFILE_POSITION_THRESHOLD
				&& Math.abs(Drive.getInstance().getHistoricalRightPosition(Drive.PROFILE_TIME_THRESHOLD)
						- Drive.getInstance().getRightPosition()) < Drive.PROFILE_POSITION_THRESHOLD
				&& Math.abs(Drive.getInstance().getHistoricalRightPosition(Drive.PROFILE_TIME_THRESHOLD.mul(2))
						- Drive.getInstance().getRightPosition()) < Drive.PROFILE_POSITION_THRESHOLD)
			return true;
		return false;
	}

}
