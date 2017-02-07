package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;

public class HoodSpeedCommand extends NRCommand{

	double speed;
	
	public HoodSpeedCommand(double speed) {
		super(Hood.getInstance());
		this.speed = speed;
	}
	
	@Override
	public void onExecute() {
		Hood.getInstance().setMotorSpeed(speed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
