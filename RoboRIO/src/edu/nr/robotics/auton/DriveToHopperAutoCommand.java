package edu.nr.robotics.auton;

import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;

public class DriveToHopperAutoCommand extends RequiredAutoCommand {

	public DriveToHopperAutoCommand() {
		addParallel(new EnableAutoTrackingCommand());
		if (Robot.side == SideOfField.blue) {
			addSequential(new MotionProfileWallToHopperCommand(FieldMap.FORWARD_DISTANCE_WALL_TO_HOPPER, FieldMap.SIDE_DISTANCE_WALL_TO_HOPPER, FieldMap.ANGLE_WALL_TO_HOPPER));
		} else {
			addSequential(new MotionProfileWallToHopperCommand(FieldMap.FORWARD_DISTANCE_WALL_TO_HOPPER, -FieldMap.SIDE_DISTANCE_WALL_TO_HOPPER, -FieldMap.ANGLE_WALL_TO_HOPPER));
		}
		if (Robot.autoShoot)
			addSequential(new AutoShootCommand());
	}
}
