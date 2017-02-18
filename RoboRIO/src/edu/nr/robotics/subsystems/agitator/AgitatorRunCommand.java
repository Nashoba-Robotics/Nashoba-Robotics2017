package edu.nr.robotics.subsystems.agitator;

import edu.nr.lib.commandbased.JoystickCommand;

public class AgitatorRunCommand extends JoystickCommand {
	
	public AgitatorRunCommand() {
		super(Agitator.getInstance());
	}
	
	@Override
	public void onStart() {
		Agitator.getInstance().setMotorVoltagePercent(Agitator.RUN_PERCENT);
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
