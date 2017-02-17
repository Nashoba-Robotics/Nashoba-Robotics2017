package edu.nr.robotics.subsystems.loader;

import edu.nr.robotics.multicommands.AutoDecideShootCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LoaderShootCommand extends CommandGroup {

	public LoaderShootCommand() {
		if (AutoDecideShootCommand.canShoot()) {
			addSequential(new LoaderRunCommand());
		}
	}
}
