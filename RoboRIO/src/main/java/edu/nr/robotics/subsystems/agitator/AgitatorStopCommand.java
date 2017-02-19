package edu.nr.robotics.subsystems.agitator;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AgitatorStopCommand extends CommandGroup {

	public AgitatorStopCommand() {
		addSequential(new AgitatorSpeedCommand(0));
	}
}
