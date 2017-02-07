package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.JoystickCommand;
import edu.nr.robotics.OI;

public class HoodJoystickCommand extends JoystickCommand{

public long joystickCheckPeriod = 0; //TODO: Hood: Find period of checking for switching to joystick control
	
	public HoodJoystickCommand() {
		super(Hood.getInstance());
	}

	@Override
	public void onExecute() {
		Hood.getInstance().setMotorSpeed(OI.getInstance().getHoodValue());
	}

	@Override
	public boolean shouldSwitchToJoystick() {
		return OI.getInstance().isHoodNonZero();
	}

	@Override
	public long getPeriodOfCheckingForSwitchToJoystick() {
		return joystickCheckPeriod;
	}
}
