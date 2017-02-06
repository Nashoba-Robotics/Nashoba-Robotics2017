package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;

public class HoodDeltaPositionCommand extends NRCommand{

	double deltaPosition;
	
	public HoodDeltaPositionCommand(double deltaPosition) {
		super(Hood.getInstance());
		this.deltaPosition = deltaPosition;
	}
	
	@Override
	public void onStart() {
		new HoodPositionCommand(Hood.getInstance().getPosition() + deltaPosition).start();
	}
}
