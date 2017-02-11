package edu.nr.robotics.subsystems.gearMover;

import edu.nr.lib.commandbased.NRCommand;

public class RetractGearCommand extends NRCommand {

	public RetractGearCommand() {
		super(GearMover.getInstance());
	}
	
}
