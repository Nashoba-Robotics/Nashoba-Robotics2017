package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class TurretDeltaPositionCommand extends NRCommand{

	Angle deltaPosition;
	
	/**
	 * 
	 * @param deltaPosition
	 */
	public TurretDeltaPositionCommand(Angle deltaPosition) {
		super(Turret.getInstance());
		this.deltaPosition = deltaPosition;
	}

	@Override
	public void onStart() {
		Turret.getInstance().setPosition(deltaPosition.add(Turret.getInstance().getPosition()));
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
