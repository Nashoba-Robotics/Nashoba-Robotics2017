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
	
	@Override
	public boolean isFinishedNR() {
		return Hood.getInstance().getPosition() < position + Hood.POSITION_THRESHOLD && Hood.getInstance().getPosition() > position - Hood.POSITION_THRESHOLD;
	}

}
