package edu.nr.robotics.subsystems.loader;

import edu.nr.lib.commandbased.NRCommand;

public class LoaderSpeedCommand extends NRCommand{

	double speed;
	
	/**
	 * 
	 * @param speed Percent voltage
	 */
	public LoaderSpeedCommand(double speed) {
		super(Loader.getInstance());
		this.speed = speed;
	}
	
	@Override
	public void onStart() {
		Loader.getInstance().setMotorVoltage(speed);
	}
}
