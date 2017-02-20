package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.StationaryTrackingCalculation;

public class TurretStationaryAngleCorrectionCommand extends NRCommand{

		
	public TurretStationaryAngleCorrectionCommand() {
		super(Turret.getInstance());
	}
	
	@Override
	public void onStart() {
		Turret.getInstance().setAutoAlign(true);
	}
	
	@Override
	public void onExecute() {
		Turret.getInstance().setPosition(StationaryTrackingCalculation.getInstance().getTurretAngle());
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
	@Override
	public void onEnd() {
		Turret.getInstance().setAutoAlign(false);
	}
}
