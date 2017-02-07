package edu.nr.lib.commandbased;

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
