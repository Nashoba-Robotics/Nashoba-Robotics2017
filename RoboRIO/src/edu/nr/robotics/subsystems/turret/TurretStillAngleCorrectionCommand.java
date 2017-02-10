package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class TurretStillAngleCorrectionCommand extends NRCommand{

	public TurretStillAngleCorrectionCommand() {
		super(Turret.getInstance());
	}
	
	@Override
	public void onExecute() {
		long angle = 0; //TODO: TurretStillAngleCorrectionCommand: Get angle from data
		angle /= 360; //Puts angle into rotations
		long timeStamp = 0;//TODO: TurretStillAngleCorrectionCommand: Get time stamp from data;
		double previousPosition = Turret.getInstance().getHistoricalPosition(timeStamp);
		double currentPosition = Turret.getInstance().getPosition();
		double deltaPosition = currentPosition - previousPosition;
		double angleToGo = angle - deltaPosition; //In rotations
		Turret.getInstance().setPositionDelta(angleToGo);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
