package edu.nr.lib.commandbased;

/**
 * A generic command to be used by {@link NRSubsystem}. 
 * It is associated with a specific subsystem and that subsystem uses the joystick command as the default command.
 * 
 * The subsystem checks {@link shouldSwitchToJoystick} every {@link getPeriodOfCheckingForSwitchToJoystick} milliseconds.
 * 
 * If {@link shouldSwitchToJoystick} returns true, then whatever command is currently running on the subsystem is cancelled,
 *   causing the joystick command to begin running. If the joystick command was the one running, no command will start.
 *   
 *   
 *
 */
public abstract class JoystickCommand extends NRCommand {

	public JoystickCommand(NRSubsystem s) {
		super(s);
	}
	
	/**
	 * This is checked by NRSubsystem to decide if the subsystem should switch back to joystick control.
	 * This is checked every getPeriodOfCheckingForSwitchToJoystick() milliseconds.
	 * @return Should the subsystem switch back to this command
	 */
	protected abstract boolean shouldSwitchToJoystick();
	
	/**
	 * How often the subsystem should check shouldSwitchToJoystick() 
	 *   to decide whether or not to switch back to the joystick command
	 * @return The period in milliseconds
	 */
	protected abstract long getPeriodOfCheckingForSwitchToJoystick();

	
	@Override
	public final boolean isFinishedNR() {
		return false;
	}
}
