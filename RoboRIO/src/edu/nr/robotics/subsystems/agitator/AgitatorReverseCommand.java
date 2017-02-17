package edu.nr.robotics.subsystems.agitator;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AgitatorReverseCommand extends CommandGroup {

	public AgitatorReverseCommand() {
		addSequential(new AgitatorSpeedCommand(-RobotMap.AGITATOR_RUN_SPEED));
	}
}
