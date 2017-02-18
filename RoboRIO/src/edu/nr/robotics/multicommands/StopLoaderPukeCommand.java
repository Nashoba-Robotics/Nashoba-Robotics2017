package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.robotics.subsystems.agitator.Agitator;
<<<<<<< HEAD
import edu.nr.robotics.subsystems.agitator.AgitatorRunCommand;
=======
>>>>>>> a135d3df9ef0bd67f77b7831351401ab0e89c5be
import edu.nr.robotics.subsystems.loader.LoaderStopCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class StopLoaderPukeCommand extends CommandGroup {

	public StopLoaderPukeCommand() {
		addParallel(new LoaderStopCommand());
		addSequential(new DoNothingCommand(Agitator.getInstance()));
	}
}
