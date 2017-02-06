package edu.nr.lib;


public abstract class JoystickCommand extends NRCommand {

	public JoystickCommand(NRSubsystem s) {
		super(s);
	}
	
	/**
	 * This is checked by NRSubsystem to decide if the subsystem should switch back to joystick control.
	 * This is checked every getPeriodOfCheckingForSwitchToJoystick() milliseconds.
	 * @return Should the subsystem switch back to this command
	 */
	public abstract boolean shouldSwitchToJoystick();
	
	/**
	 * How often the subsystem should check shouldSwitchToJoystick() to decide whether or not to switch back to the joystick command
	 * @return The period in milliseconds
	 */
	public abstract long getPeriodOfCheckingForSwitchToJoystick();

	
	@Override
	public final boolean isFinishedNR() {
		return false;
	}
}
