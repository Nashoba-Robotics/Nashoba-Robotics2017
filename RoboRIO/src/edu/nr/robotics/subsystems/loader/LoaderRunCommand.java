package edu.nr.robotics.subsystems.loader;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LoaderRunCommand extends CommandGroup{
	
	public LoaderRunCommand() {
		addSequential(new LoaderSpeedCommand(RobotMap.LOADER_RUN_SPEED));
	}
}
