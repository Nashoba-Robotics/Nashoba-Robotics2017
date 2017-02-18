package edu.nr.robotics.auton;

import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;

public class DriveToHopperAutoCommand extends RequiredAutoCommand {

	public DriveToHopperAutoCommand() {
		super();
		addParallel(new EnableAutoTrackingCommand());
		if (Robot.side == SideOfField.blue) {
			addSequential(new MotionProfileWallToHopperCommand(RobotMap.FORWARD_DISTANCE_WALL_TO_HOPPER, -RobotMap.SIDE_DISTANCE_WALL_TO_HOPPER, -RobotMap.ANGLE_WALL_TO_HOPPER));
		} else {
			addSequential(new MotionProfileWallToHopperCommand(RobotMap.FORWARD_DISTANCE_WALL_TO_HOPPER, RobotMap.SIDE_DISTANCE_WALL_TO_HOPPER, RobotMap.ANGLE_WALL_TO_HOPPER));
		}
		if (Robot.autoShoot)
			addSequential(new AutoShootCommand());
	}
}
