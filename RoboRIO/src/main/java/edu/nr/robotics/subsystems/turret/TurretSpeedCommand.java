package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.AngularSpeed;

public class TurretSpeedCommand extends NRCommand {

	AngularSpeed speed;
	
	/**
	 * 
	 * @param speed in degrees per second
	 */
	public TurretSpeedCommand(AngularSpeed speed) {
		super(Turret.getInstance());
		this.speed = speed;
	}
	
	@Override
	public void onStart() {
		Turret.getInstance().setMotorSpeedInDegreesPerSecond(speed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
