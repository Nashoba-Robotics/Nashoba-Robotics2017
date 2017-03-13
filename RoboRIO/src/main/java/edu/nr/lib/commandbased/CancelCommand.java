package edu.nr.lib.commandbased;

/**
 * Cancels all running commands and calls {@link NRSubsystem#disable()} on all subsystems
 *
 */
public class CancelCommand extends NRCommand {

	NRSubsystem subsystem;
	
	public CancelCommand(NRSubsystem subsystem) {
		super(subsystem);
		this.subsystem = subsystem;
	}

	@Override
	public void onStart() {
		subsystem.disable();
	}
	
}
