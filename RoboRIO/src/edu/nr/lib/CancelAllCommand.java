package edu.nr.lib;

public class CancelAllCommand extends NRCommand {

	@Override
	public void onStart() {
		for (NRSubsystem subsystem : NRSubsystem.subsystems) {
			subsystem.disable();
		}
	}
	
}
