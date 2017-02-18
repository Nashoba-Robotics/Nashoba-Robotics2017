package edu.nr.robotics.subsystems.agitator;

import edu.nr.lib.commandbased.NRCommand;

public class AgitatorRunCommand extends NRCommand {

	double percent;
	
	public AgitatorRunCommand(double percent) {
		super(Agitator.getInstance());
		this.percent = percent;
	}

	@Override
	public void onExecute() {
		Agitator.getInstance().setMotorSpeed(percent);
	}
	
}
