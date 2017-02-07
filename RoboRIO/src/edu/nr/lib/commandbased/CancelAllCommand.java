package edu.nr.lib.commandbased;

/**
 * Cancels all running commands and calls {@link NRSubsystem#disable()} on all subsystems
 *
 */
public class CancelAllCommand extends NRCommand {

	public CancelAllCommand() {
		super(NRSubsystem.subsystems);
	}

	@Override
	public void onStart() {
		for (NRSubsystem subsystem : NRSubsystem.subsystems) {
			subsystem.disable();
		}
	}
	
}
