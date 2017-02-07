package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;

public class HoodPositionCommand extends NRCommand{

	double position;
	
	public HoodPositionCommand(double position) {
		super(Hood.getInstance());
		this.position = position;
	}

	@Override
	public void onExecute() {
		Hood.getInstance().setPosition(position);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
