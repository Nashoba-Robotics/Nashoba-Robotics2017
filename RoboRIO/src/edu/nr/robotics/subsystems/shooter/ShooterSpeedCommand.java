package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.NRCommand;

public class ShooterSpeedCommand extends NRCommand {

	double speed;
	
	public ShooterSpeedCommand(double speed) {
		super(Shooter.getInstance());
		this.speed = speed;
	}
	
	@Override
	public void onExecute() {
		Shooter.getInstance().setMotorSpeed(speed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}

}
