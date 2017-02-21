package edu.nr.robotics.subsystems.loader;

import edu.nr.lib.commandbased.NRCommand;

public class LoaderSpeedCommand extends NRCommand{

	double lowSpeed;
	double highSpeed;
	
	/**
	 * 
	 * @param speed Percent voltage
	 */
	public LoaderSpeedCommand(double lowSpeed, double highSpeed) {
		super(Loader.getInstance());
		this.lowSpeed = lowSpeed;
		this.highSpeed = highSpeed;
	}
	
	@Override
	public void onStart() {
		Loader.getInstance().setLowMotorVoltage(lowSpeed);
		Loader.getInstance().setHighMotorVoltage(highSpeed);
	}
}
