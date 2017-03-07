package edu.nr.robotics.subsystems.gearMover;

import edu.nr.lib.commandbased.NRCommand;

public class GearFlapInCommand extends NRCommand {

	public GearFlapInCommand() {
		super(GearMover.getInstance());
	}
	
	public void onStart() {
		GearMover.getInstance().inGearGetPosition();
	}
	
}
