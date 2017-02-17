package edu.nr.robotics.subsystems.agitator;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.subsystems.loader.Loader;

public class AgitatorSpeedCommand extends NRCommand {

	double percent;
	
	public AgitatorSpeedCommand(double percent) {
		super(Agitator.getInstance());
		this.percent = percent;
	}
	
	@Override
	public void onExecute() {
		Loader.getInstance().setMotorSpeed(percent);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
