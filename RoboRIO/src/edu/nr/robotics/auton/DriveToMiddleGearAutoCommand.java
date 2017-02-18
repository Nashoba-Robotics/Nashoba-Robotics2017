package edu.nr.robotics.auton;

import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;

public class DriveToMiddleGearAutoCommand extends RequiredAutoCommand {

	public DriveToMiddleGearAutoCommand() {
		super();
		addParallel(new EnableAutoTrackingCommand());
		addSequential(new DriveForwardCommand(FieldMap.DISTANCE_TO_CENTER_PEG));
		if (Robot.autoShoot) {
			addSequential(new AutoShootCommand());
		}
	}
}
