package edu.nr.robotics.subsystems.loader;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LoaderReverseCommand extends CommandGroup{
	
	public LoaderReverseCommand() {
		addSequential(new LoaderSpeedCommand(Loader.LOW_REVERSE_VOLTAGE, Loader.HIGH_REVERSE_VOLTAGE));
	}
}
