package edu.nr.robotics.subsystems.agitator;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.shooter.Shooter;

public class AgitatorRunCommand extends JoystickCommand {
	
	public AgitatorRunCommand() {
		super(Agitator.getInstance());
	}
	
	@Override
	public void onStart() {
		if (OI.getInstance().isShooterOn()) {
			Agitator.getInstance().setMotorVoltage(Agitator.HIGH_RUN_PERCENT);
		} else {
			Agitator.getInstance().setMotorVoltage(Agitator.LOW_RUN_PERCENT);
		}
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
