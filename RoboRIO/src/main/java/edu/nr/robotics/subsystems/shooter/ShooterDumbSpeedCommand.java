package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;

public class ShooterDumbSpeedCommand extends NRCommand {

	double speed;
	
	public ShooterDumbSpeedCommand(double speed) {
		super(Shooter.getInstance());
		this.speed = speed;
	}
	
	@Override
	public void onStart() {
		Shooter.getInstance().setMotorSpeedPercent(speed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}

}
