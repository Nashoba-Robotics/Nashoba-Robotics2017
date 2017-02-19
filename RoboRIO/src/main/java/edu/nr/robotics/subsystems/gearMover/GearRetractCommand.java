package edu.nr.robotics.subsystems.gearMover;

import edu.nr.lib.commandbased.NRCommand;

public class GearRetractCommand extends NRCommand {

	public GearRetractCommand() {
		super(GearMover.getInstance());
	}
	
	public void onStart() {
		GearMover.getInstance().retractGearMover();
	}
	
}
