package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class TurretDeltaPositionCommand extends NRCommand{

	double deltaPosition;
	
	/**
	 * 
	 * @param deltaPosition degrees
	 */
	public TurretDeltaPositionCommand(double deltaPosition) {
		super(Turret.getInstance());
		this.deltaPosition = deltaPosition;
	}

	@Override
	public void onStart() {
		Turret.getInstance().setPositionDelta(deltaPosition);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
