package edu.nr.robotics.auton;

import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveToMiddleGearAutoCommand extends CommandGroup {

	public DriveToMiddleGearAutoCommand() {
		addParallel(new ZeroThenAutoTrackCommand());
		addSequential(new DriveForwardProfilingCommand(FieldMap.DISTANCE_TO_CENTER_PEG.negate())); //Negated for driving backwards in auto
		if (Robot.autoShoot) {
			addSequential(new AlignThenShootCommand());
		}
	}
}
