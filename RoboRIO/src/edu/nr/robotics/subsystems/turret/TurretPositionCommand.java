package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.NRCommand;

public class TurretPositionCommand extends NRCommand{

	double position;
	
	public TurretPositionCommand(double position) {
		super(Turret.getInstance());
		this.position = position;
	}

	@Override
	public void onExecute() {
		Turret.getInstance().setPosition(position);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
