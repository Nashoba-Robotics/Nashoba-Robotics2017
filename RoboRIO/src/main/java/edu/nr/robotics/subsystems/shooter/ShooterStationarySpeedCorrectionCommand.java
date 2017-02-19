package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.StationaryTrackingCalculation;

public class ShooterStationarySpeedCorrectionCommand extends NRCommand {
	
	public ShooterStationarySpeedCorrectionCommand() {
		super(Shooter.getInstance());
	}
	
	@Override
	public void onStart() {
		Shooter.getInstance().setAutoAlign(true);
	}
	
	@Override
	public void onExecute() {		
		Shooter.getInstance().setMotorSpeedInRPM(StationaryTrackingCalculation.getInstance().getShooterSpeed());
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
	@Override
	public void onEnd() {
		Shooter.getInstance().setAutoAlign(false);
	}
}
