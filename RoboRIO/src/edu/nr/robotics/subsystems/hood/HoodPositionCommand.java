package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;

public class HoodPositionCommand extends NRCommand{

	double position;
	
	public HoodPositionCommand(double position) {
		super(Hood.getInstance());
		this.position = position;
	}

	@Override
	public void onStart() {
		Hood.getInstance().setPosition(position);
	}
}
