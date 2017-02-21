package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.AngularSpeed;

public class ShooterDeltaSpeedCommand extends NRCommand {

	AngularSpeed speedDelta;
	AngularSpeed initialSpeed;
	
	public ShooterDeltaSpeedCommand(AngularSpeed speedDelta) {
		super(Shooter.getInstance());
		this.speedDelta = speedDelta;
	}
	
	@Override
	public void onStart() {
		initialSpeed = Shooter.getInstance().getSpeed();
		Shooter.getInstance().setMotorSpeedInRPM(initialSpeed.add(speedDelta));
	}

}
