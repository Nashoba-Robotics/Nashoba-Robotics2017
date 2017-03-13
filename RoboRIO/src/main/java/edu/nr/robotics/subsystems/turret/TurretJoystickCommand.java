package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;

public class TurretJoystickCommand extends JoystickCommand {

	public long joystickCheckPeriod = 100;
	
	public TurretJoystickCommand() {
		super(Turret.getInstance());
	}

	@Override
	public void onExecute() {
		Turret.getInstance().setMotorSpeedInPercent(OI.getInstance().getTurretValue()/4);
	}

	@Override
	public boolean shouldSwitchToJoystick() {
		return OI.getInstance().isTurretNonZero();
	}

	@Override
	public long getPeriodOfCheckingForSwitchToJoystick() {
		return joystickCheckPeriod;
	}
}
