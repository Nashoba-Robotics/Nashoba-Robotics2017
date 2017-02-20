package edu.nr.robotics.subsystems.loader;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LoaderRunCommand extends CommandGroup{
	
	public LoaderRunCommand() {
		addSequential(new LoaderSpeedCommand(Loader.LOW_RUN_VOLTAGE, Loader.HIGH_RUN_VOLTAGE));
	}
}
