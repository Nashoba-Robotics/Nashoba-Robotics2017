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
		Shooter.getInstance().setMotorSpeedInRPM(speed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}

}
