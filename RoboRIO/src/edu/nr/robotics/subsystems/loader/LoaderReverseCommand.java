package edu.nr.robotics.subsystems.loader;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LoaderReverseCommand extends CommandGroup{
	
	public LoaderReverseCommand() {
		addSequential(new LoaderSpeedCommand(RobotMap.LOADER_REVERSE_SPEED));
	}
}
