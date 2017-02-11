package edu.nr.robotics.subsystems.gearMover;

import edu.nr.lib.commandbased.NRCommand;

public class GetGearPositionInCommand extends NRCommand {

	public GetGearPositionInCommand() {
		super(GearMover.getInstance());
	}
	
}
