package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;

public class HoodSpeedCommand extends NRCommand{

	double speed;
	
	/**
	 * Degrees per second
	 */
	public HoodSpeedCommand(double speed) {
		super(Hood.getInstance());
		this.speed = speed;
	}
	
	@Override
	public void onStart() {
		Hood.getInstance().setMotorSpeedInDegreesPerSecond(speed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
