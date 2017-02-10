package edu.nr.robotics.subsystems.loader;

import edu.nr.lib.commandbased.NRCommand;

public class LoaderSpeedCommand extends NRCommand{

	double speed;
	
	public LoaderSpeedCommand(double speed) {
		super(Loader.getInstance());
		this.speed = speed;
	}
	
	@Override
	public void onExecute() {
		Loader.getInstance().setMotorSpeed(speed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
