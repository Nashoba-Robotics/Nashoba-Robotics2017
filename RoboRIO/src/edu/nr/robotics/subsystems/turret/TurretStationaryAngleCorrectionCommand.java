package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.network.TCPServer;

public class TurretStationaryAngleCorrectionCommand extends NRCommand{

	public TurretStationaryAngleCorrectionCommand() {
		super(Turret.getInstance());
	}
	
	@Override
	public void onExecute() {
		long angle = TCPServer.getInstance().getValue('a'); //TODO: TurretStillAngleCorrectionCommand: Get angle from data
		angle /= 360; //Puts angle into rotations
		long timeStamp = TCPServer.getInstance().getValue('t');//TODO: TurretStillAngleCorrectionCommand: Get time stamp from data
		long currentTime = (long) (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() * 1000);
		double deltaTime = currentTime - timeStamp;
		double previousPosition = Turret.getInstance().getHistoricalPosition(deltaTime);
		double currentPosition = Turret.getInstance().getPosition();
		double deltaPosition = currentPosition - previousPosition;
		double angleToGo = angle + deltaPosition; //In rotations
		Turret.getInstance().setPositionDelta(angleToGo);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
