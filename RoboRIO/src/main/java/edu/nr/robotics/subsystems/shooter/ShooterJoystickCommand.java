package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.JoystickCommand;

public class ShooterJoystickCommand extends JoystickCommand {

	public ShooterJoystickCommand() {
		super(Shooter.getInstance());
	}
	
	@Override
	public void onExecute() {
		//Shooter.getInstance().setMotorSpeedInRPM(Shooter.getInstance().motorSetpoint);
		Shooter.getInstance().setMotorSpeedPercent(0.30);
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
