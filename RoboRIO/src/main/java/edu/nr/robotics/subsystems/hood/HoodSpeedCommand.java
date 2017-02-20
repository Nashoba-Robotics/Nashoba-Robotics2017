package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.AngularSpeed;

public class HoodSpeedCommand extends NRCommand{

	AngularSpeed speed;
	
	public HoodSpeedCommand(AngularSpeed speed) {
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
