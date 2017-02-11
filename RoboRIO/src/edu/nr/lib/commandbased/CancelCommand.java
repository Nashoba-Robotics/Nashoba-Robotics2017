package edu.nr.lib.commandbased;

/**
 * Cancels all running commands and calls {@link NRSubsystem#disable()} on all subsystems
 *
 */
public class CancelCommand extends NRCommand {

	public CancelCommand(NRSubsystem subsystem) {
		super(subsystem);
	}

	@Override
	public void onStart() {
		for (NRSubsystem subsystem : this.subsystems) {
			subsystem.disable();
		}
	}
	
}
