package edu.nr.robotics.subsystems.agitator;

import edu.nr.lib.commandbased.JoystickCommand;

public class AgitatorRunCommand extends JoystickCommand {
	
	public AgitatorRunCommand() {
		super(Agitator.getInstance());
	}

	@Override
	protected boolean shouldSwitchToJoystick() {
		return false;
	}

	@Override
	protected long getPeriodOfCheckingForSwitchToJoystick() {
		return Long.MAX_VALUE;
	}
	
}
