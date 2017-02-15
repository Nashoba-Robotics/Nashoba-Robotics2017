package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.OI;

public class ShooterSpeedCommand extends NRCommand {

	double speed;
	
	public ShooterSpeedCommand(double speed) {
		super(Shooter.getInstance());
		this.speed = speed;
	}
	
	@Override
	public void onExecute() {
		Shooter.getInstance().setAutoAlign(false);
		if (OI.getInstance().isShooterOn()) {
			Shooter.getInstance().setMotorSpeed(speed);
		} else {
			Shooter.getInstance().setMotorSpeed(0);
		}
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}

}
