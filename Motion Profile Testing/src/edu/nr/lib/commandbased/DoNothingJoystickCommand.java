package edu.nr.lib.commandbased;

/**
 * A {@link JoystickCommand} that doesn't do anything
 *
 */
public final class DoNothingJoystickCommand extends JoystickCommand {
	
	/**
	 * Create the command
	 * @param subsystem The subsystem to associate the command with
	 */
	public DoNothingJoystickCommand(NRSubsystem s) {
		super(s);
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
