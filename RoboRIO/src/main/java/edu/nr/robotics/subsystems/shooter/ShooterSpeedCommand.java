package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.AngularSpeed;

public class ShooterSpeedCommand extends NRCommand {

	AngularSpeed speed;
	
	public ShooterSpeedCommand(AngularSpeed speed) {
		super(Shooter.getInstance());
		this.speed = speed;
	}
	
	@Override
	public void onStart() {
		Shooter.getInstance().setMotorSpeedInRPM(speed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}

}
