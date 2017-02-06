package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.NRCommand;

public class TurretDeltaPositionCommand extends NRCommand{

	double deltaPosition;
	
	public TurretDeltaPositionCommand(double deltaPosition) {
		super(Turret.getInstance());
		this.deltaPosition = deltaPosition;
	}
	
	@Override
	public void onStart() {
		new TurretPositionCommand(Turret.getInstance().getPosition() + deltaPosition).start();
	}
}
