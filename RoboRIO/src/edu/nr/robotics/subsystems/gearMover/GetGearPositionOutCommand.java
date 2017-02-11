package edu.nr.robotics.subsystems.gearMover;

import edu.nr.lib.commandbased.NRCommand;

public class GetGearPositionOutCommand extends NRCommand {

	public GetGearPositionOutCommand() {
		super(GearMover.getInstance());
	}
	
}
