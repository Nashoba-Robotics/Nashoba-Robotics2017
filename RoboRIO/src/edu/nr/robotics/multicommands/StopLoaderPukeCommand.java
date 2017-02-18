package edu.nr.robotics.multicommands;

import edu.nr.robotics.subsystems.agitator.AgitatorRunCommand;
import edu.nr.robotics.subsystems.loader.LoaderStopCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class StopLoaderPukeCommand extends CommandGroup {

	public StopLoaderPukeCommand() {
		addParallel(new LoaderStopCommand());
		addSequential(new AgitatorRunCommand());
	}
}
