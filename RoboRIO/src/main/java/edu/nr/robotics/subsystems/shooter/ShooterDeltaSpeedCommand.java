package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;

public class ShooterDeltaSpeedCommand extends NRCommand {

	AngularSpeed speedDelta;
	AngularSpeed initialSpeed;
	
	public ShooterDeltaSpeedCommand(AngularSpeed speedDelta) {
		super(Shooter.getInstance());
		this.speedDelta = speedDelta;
	}
	
	@Override
	public void onStart() {
		initialSpeed = Shooter.getInstance().motorSetpoint;
		Shooter.getInstance().setMotorSpeedInRPM(initialSpeed.add(speedDelta));
		System.out.println("Initial Setpoint: " + initialSpeed.get(Angle.Unit.ROTATION, Time.Unit.SECOND));
		System.out.println("Speed delta: " + speedDelta.get(Angle.Unit.ROTATION, Time.Unit.SECOND));
		System.out.println("Final Setpoint: " + initialSpeed.add(speedDelta).get(Angle.Unit.ROTATION, Time.Unit.SECOND));
		
	}

}
