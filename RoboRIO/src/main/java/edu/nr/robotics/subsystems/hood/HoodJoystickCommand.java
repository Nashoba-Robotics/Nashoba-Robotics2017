package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;

public class HoodJoystickCommand extends JoystickCommand{

	/**
	 * Milliseconds
	 */
	public long joystickCheckPeriod = 100;
	
	public HoodJoystickCommand() {
		super(Hood.getInstance());
	}

	@Override
	public void onExecute() {
		Hood.getInstance().setMotorSpeedInPercent(OI.getInstance().getHoodValue());
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
