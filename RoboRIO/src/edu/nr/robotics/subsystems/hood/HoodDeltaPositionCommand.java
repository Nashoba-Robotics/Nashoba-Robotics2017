package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;

public class HoodDeltaPositionCommand extends NRCommand{

	double deltaPosition;
	double goalPosition;
	
	public HoodDeltaPositionCommand(double deltaPosition) {
		super(Hood.getInstance());
		this.deltaPosition = deltaPosition;
	}
	
	@Override
	public void onStart() {
		Hood.getInstance().setAutoAlign(false);
		goalPosition = Hood.getInstance().getPosition() + deltaPosition;
		Hood.getInstance().setPosition(goalPosition);
	}
	
	@Override
	public boolean isFinishedNR() {
		return Hood.getInstance().getPosition() < goalPosition + Hood.POSITION_THRESHOLD && Hood.getInstance().getPosition() > goalPosition - Hood.POSITION_THRESHOLD;
	}
}
