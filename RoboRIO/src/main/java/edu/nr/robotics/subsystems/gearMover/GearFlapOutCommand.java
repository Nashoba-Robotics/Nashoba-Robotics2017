package edu.nr.robotics.subsystems.gearMover;

import edu.nr.lib.commandbased.NRCommand;

public class GearFlapOutCommand extends NRCommand {

	public GearFlapOutCommand() {
		super(GearMover.getInstance());
	}
	
	public void onStart() {
		GearMover.getInstance().outGearGetPosition();
	}
	
}
