package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;

public class TurretJoystickCommand extends JoystickCommand {

	public long joystickCheckPeriod = 0; //TODO: Turret: Find period of checking for switching to joystick control
	
	public TurretJoystickCommand() {
		super(Turret.getInstance());
	}

	@Override
	public void onExecute() {
		Turret.getInstance().setMotorSpeed(OI.getInstance().getTurretValue());
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
