package edu.nr.robotics.auton;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.motionprofiling.TwoDimensionalMotionProfilerPathfinder;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
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
	boolean negate;
	Distance startPosition;

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

	public MotionProfileToSideGearCommand(Distance forwardDistance, Distance sideDistance, Angle endHeading, boolean negate) {
		super(Drive.getInstance());
		this.forwardDistance = forwardDistance;
		this.sideDistance = sideDistance;
		this.endHeading = endHeading;
		this.negate = negate;
	}

	@Override
	public void onStart() {
		startPosition = Drive.getInstance().getLeftPosition();
		if (Drive.getInstance().getCurrentGear() == Gear.low) {
			profiler = new TwoDimensionalMotionProfilerPathfinder(Drive.getInstance(), Drive.getInstance(), KV, KA, KP,
					KI, KD, KP_THETA,
					Drive.MAX_LOW_GEAR_SPEED.get(Distance.Unit.METER, Time.Unit.SECOND)
							* MAX_SPEED_PERCENTAGE,
					Drive.MAX_ACCELERATION.get(Distance.Unit.METER, Time.Unit.SECOND, Time.Unit.SECOND)
							* MAX_SPEED_PERCENTAGE,
					Drive.MAX_JERK.get(Distance.Unit.METER, Time.Unit.SECOND, Time.Unit.SECOND, Time.Unit.SECOND),
					Drive.TICKS_PER_REV, Drive.WHEEL_DIAMETER.get(Distance.Unit.METER), Drive.WHEEL_BASE.get(Distance.Unit.METER), this.negate);
		} else {
			profiler = new TwoDimensionalMotionProfilerPathfinder(Drive.getInstance(), Drive.getInstance(), KV, KA, KP,
					KI, KD, KP_THETA,
					Drive.MAX_HIGH_GEAR_SPEED.get(Distance.Unit.METER, Time.Unit.SECOND)
							* MAX_SPEED_PERCENTAGE,
					Drive.MAX_ACCELERATION.get(Distance.Unit.METER, Time.Unit.SECOND, Time.Unit.SECOND)
							* MAX_SPEED_PERCENTAGE,
					Drive.MAX_JERK.get(Distance.Unit.METER, Time.Unit.SECOND, Time.Unit.SECOND, Time.Unit.SECOND),
					Drive.TICKS_PER_REV, Drive.WHEEL_DIAMETER.get(Distance.Unit.METER), Drive.WHEEL_BASE.get(Distance.Unit.METER), this.negate);
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
		boolean finished = (Drive.getInstance().getHistoricalLeftPosition(Drive.PROFILE_TIME_THRESHOLD).sub(Drive.getInstance().getLeftPosition())).abs()
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
				&& (Drive.getInstance().getHistoricalLeftPosition(Drive.PROFILE_TIME_THRESHOLD.mul(2)).sub(Drive.getInstance().getLeftPosition())).abs()
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
				&& (Drive.getInstance().getHistoricalRightPosition(Drive.PROFILE_TIME_THRESHOLD).sub(Drive.getInstance().getRightPosition())).abs()
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
				&& (Drive.getInstance().getHistoricalRightPosition(Drive.PROFILE_TIME_THRESHOLD.mul(2)).sub(Drive.getInstance().getRightPosition())).abs()
				.lessThan(Drive.PROFILE_POSITION_THRESHOLD)
				&& (Drive.getInstance().getLeftPosition().sub(startPosition)).abs().greaterThan(Drive.PROFILE_POSITION_THRESHOLD);
		return finished;
	}

}
