package edu.nr.robotics.auton;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;

public class DriveToMiddleGearAutoCommand extends RequiredAutoCommand {

	public DriveToMiddleGearAutoCommand() {
		super();
		addSequential(new DriveForwardCommand(RobotMap.DISTANCE_TO_CENTER_PEG));
	}
}
