package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;

public class HoodStopCommand extends NRCommand{

	public HoodStopCommand() {
		super(Hood.getInstance());
	}

	@Override
	public void onExecute() {
		Hood.getInstance().disable();
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
