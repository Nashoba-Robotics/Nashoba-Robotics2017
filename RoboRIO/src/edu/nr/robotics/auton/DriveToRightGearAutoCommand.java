package edu.nr.robotics.auton;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.MotionProfileToSideGearCommand;

public class DriveToRightGearAutoCommand extends RequiredAutoCommand {

	public DriveToRightGearAutoCommand() {
		super();
		addSequential(new MotionProfileToSideGearCommand(RobotMap.FORWARD_DISTANCE_TO_SIDE_PEG, RobotMap.SIDE_DISTANCE_TO_RIGHT_PEG, RobotMap.ANGLE_TO_SIDE_PEG));
		addSequential(new GearPegAlignCommand());
	}
}
