package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Time;

public class ShooterJoystickCommand extends JoystickCommand {

	public ShooterJoystickCommand() {
		super(Shooter.getInstance());
	}
	
	@Override
	public void onExecute() {
		//Shooter.getInstance().setMotorSpeedInRPM(new AngularSpeed(0.30, Angle.Unit.DEGREE, Time.Unit.SECOND));
		if(Shooter.getInstance().motorSetpoint.get(Angle.Unit.defaultUnit, Time.Unit.defaultUnit) == 0) {
			Shooter.getInstance().setMotorSpeedInRPM(Shooter.getInstance().defaultSpeed);			
		} else {
			Shooter.getInstance().setMotorSpeedInRPM(Shooter.getInstance().motorSetpoint);
		}
		//Shooter.getInstance().setMotorSpeedPercent(0.50);
	}

	@Override
	protected boolean shouldSwitchToJoystick() {
		return false;
	}

	@Override
	protected long getPeriodOfCheckingForSwitchToJoystick() {
		return Long.MAX_VALUE;
	}
}
