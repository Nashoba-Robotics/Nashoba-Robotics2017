package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;

public class HoodStopCommand extends NRCommand{

	public HoodStopCommand() {
		super(Hood.getInstance());
	}

	@Override
	public void onStart() {
		Hood.getInstance().disable();
	}
	
}
