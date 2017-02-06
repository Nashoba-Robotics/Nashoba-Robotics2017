package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.NRCommand;

public class TurretStopCommand extends NRCommand{
	
	public TurretStopCommand() {
		super(Turret.getInstance());
	}

	@Override
	public void onExecute() {
		Turret.getInstance().disable();
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
