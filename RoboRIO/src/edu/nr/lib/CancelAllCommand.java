package edu.nr.lib;


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
