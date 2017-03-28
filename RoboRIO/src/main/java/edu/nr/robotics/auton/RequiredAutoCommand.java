package edu.nr.robotics.auton;

import edu.nr.robotics.subsystems.drive.DriveLowGearCommand;
import edu.nr.robotics.subsystems.gearMover.GearDeployCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public final class RequiredAutoCommand extends CommandGroup {

	public RequiredAutoCommand() {
		addSequential(new DriveLowGearCommand());
		addSequential(new GearDeployCommand());
	}
}
