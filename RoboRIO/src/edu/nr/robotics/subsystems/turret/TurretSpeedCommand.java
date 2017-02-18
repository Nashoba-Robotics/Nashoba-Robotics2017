package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class TurretSpeedCommand extends NRCommand {

	double speed;
	
	public TurretSpeedCommand(double speed) {
		super(Turret.getInstance());
		this.speed = speed;
	}
	
	@Override
	public void onStart() {
		Turret.getInstance().setMotorSpeed(speed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
