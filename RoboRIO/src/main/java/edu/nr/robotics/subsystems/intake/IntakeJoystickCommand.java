package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;

public class IntakeJoystickCommand extends JoystickCommand {
	
	public IntakeJoystickCommand() {
		super(Intake.getInstance());
	}
	
	@Override
	public void onExecute() {
		Intake.getInstance().setMotorVoltage(OI.getInstance().getIntakeValue());
	}

	@Override
	public boolean shouldSwitchToJoystick() {
		return false;
	}

	@Override
	public long getPeriodOfCheckingForSwitchToJoystick() {
		return Long.MAX_VALUE;  
	}

}
