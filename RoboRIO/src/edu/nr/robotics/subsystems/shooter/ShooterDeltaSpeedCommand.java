package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;

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
		Shooter.getInstance().setMotorSpeedInRPM(initialSpeed + speedDelta);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}

}
