package edu.nr.robotics.subsystems.gearMover;

import edu.nr.lib.commandbased.NRCommand;

public class GearGetPositionInCommand extends NRCommand {

	public GearGetPositionInCommand() {
		super(GearMover.getInstance());
	}
	
}
