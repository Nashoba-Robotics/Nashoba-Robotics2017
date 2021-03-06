package edu.nr.robotics.subsystems.agitator;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;

public class AgitatorRunCommand extends JoystickCommand {
	
	public AgitatorRunCommand() {
		super(Agitator.getInstance());
	}
	
	@Override
	public void onExecute() {
		if(OI.getInstance().isAgitatorOn()) {
			if (OI.getInstance().isShooterOn()) {
				Agitator.getInstance().setMotorVoltage(Agitator.HIGH_RUN_PERCENT);
			} else {
				Agitator.getInstance().setMotorVoltage(Agitator.LOW_RUN_PERCENT);
			}
		} else {
			Agitator.getInstance().setMotorVoltage(0);
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
