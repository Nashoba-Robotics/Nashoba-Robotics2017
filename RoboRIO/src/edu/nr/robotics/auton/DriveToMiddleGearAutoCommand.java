package edu.nr.robotics.auton;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.nr.robotics.subsystems.gearMover.GearDeployCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveToMiddleGearAutoCommand extends CommandGroup {

	public DriveToMiddleGearAutoCommand() {
		addParallel(new GearDeployCommand());
		addSequential(new DriveForwardCommand(RobotMap.DISTANCE_TO_CENTER_PEG));
	}
}
