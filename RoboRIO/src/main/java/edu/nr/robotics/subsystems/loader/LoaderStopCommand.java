package edu.nr.robotics.subsystems.loader;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LoaderStopCommand extends CommandGroup{

	public LoaderStopCommand() {
		addSequential(new LoaderSpeedCommand(0));
	}
}
