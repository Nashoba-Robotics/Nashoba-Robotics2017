package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class TurretPositionCommand extends NRCommand{

	double position; //In rotations
	
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
