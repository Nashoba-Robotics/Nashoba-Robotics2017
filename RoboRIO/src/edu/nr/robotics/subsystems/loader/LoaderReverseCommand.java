package edu.nr.robotics.subsystems.loader;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LoaderReverseCommand extends CommandGroup{
	
	public LoaderReverseCommand() {
		addSequential(new LoaderSpeedCommand(Loader.REVERSE_VOLTAGE));
	}
}
