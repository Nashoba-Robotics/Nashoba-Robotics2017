package edu.nr.robotics.subsystems.agitator;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AgitatorReverseCommand extends CommandGroup {

	public AgitatorReverseCommand() {
		addSequential(new AgitatorSpeedCommand(Agitator.REVERSE_PERCENT));
	}
}
