package edu.nr.robotics.subsystems.agitator;

import edu.nr.lib.commandbased.NRCommand;

public class AgitatorSpeedCommand extends NRCommand {

	double percent;
	
	public AgitatorSpeedCommand(double percent) {
		super(Agitator.getInstance());
		this.percent = percent;
	}
	
	@Override
	public void onStart() {
		Agitator.getInstance().setMotorVoltagePercent(percent);
	}
}
