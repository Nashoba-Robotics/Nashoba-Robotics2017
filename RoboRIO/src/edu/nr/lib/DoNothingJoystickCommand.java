package edu.nr.lib;

public class DoNothingJoystickCommand extends JoystickCommand {
	
	public DoNothingJoystickCommand(NRSubsystem s) {
		super(s);
	}

	@Override
	public boolean shouldSwitchToJoystick() {
		return false;
	}

	@Override
	public long getPeriodOfCheckingForSwitchToJoystick() {
		return Long.MAX_VALUE;
	}
	
}
