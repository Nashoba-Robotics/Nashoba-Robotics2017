package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.OI;

public class ShooterDeltaSpeedCommand extends NRCommand {

	double speedDelta;
	double initialSpeed;
	
	public ShooterDeltaSpeedCommand(double speedDelta) {
		super(Shooter.getInstance());
		this.speedDelta = speedDelta;
	}
	
	@Override
	public void onExecute() {
		Shooter.getInstance().setAutoAlign(false);
		initialSpeed = Shooter.getInstance().getSpeed();
		if (OI.getInstance().isShooterOn()) {
			Shooter.getInstance().setMotorSpeed(initialSpeed + speedDelta);
		} else {
			Shooter.getInstance().setMotorSpeed(0);
		}
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}

}
