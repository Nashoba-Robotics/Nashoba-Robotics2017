package edu.nr.robotics.auton;

import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.multicommands.GearPegAlignCommand;

public class DriveToShooterSideGearAutoCommand extends RequiredAutoCommand {

	public DriveToShooterSideGearAutoCommand() {
		super();
		addParallel(new EnableAutoTrackingCommand());
		if (Robot.side == SideOfField.blue) {
			addSequential(new MotionProfileToSideGearCommand(RobotMap.FORWARD_DISTANCE_TO_SIDE_PEG, RobotMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG, RobotMap.ANGLE_TO_SIDE_PEG));
		}
		else {
			addSequential(new MotionProfileToSideGearCommand(RobotMap.FORWARD_DISTANCE_TO_SIDE_PEG, -RobotMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG, -RobotMap.ANGLE_TO_SIDE_PEG));
		}
		addSequential(new GearPegAlignCommand());
		if (Robot.autoShoot) {
			addSequential(new AutoShootCommand());
		}
	}
}
