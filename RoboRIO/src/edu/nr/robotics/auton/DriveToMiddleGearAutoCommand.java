package edu.nr.robotics.auton;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveToMiddleGearAutoCommand extends RequiredAutoCommand {

	public DriveToMiddleGearAutoCommand() {
		super();
		addSequential(new DriveForwardCommand(RobotMap.DISTANCE_TO_CENTER_PEG));
	}
}
