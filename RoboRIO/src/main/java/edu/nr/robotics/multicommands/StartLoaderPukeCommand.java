package edu.nr.robotics.multicommands;

import edu.nr.robotics.subsystems.agitator.AgitatorReverseCommand;
import edu.nr.robotics.subsystems.loader.LoaderReverseCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class StartLoaderPukeCommand extends CommandGroup {

	public StartLoaderPukeCommand() {
		addParallel(new LoaderReverseCommand());
		addSequential(new AgitatorReverseCommand());
	}
}
