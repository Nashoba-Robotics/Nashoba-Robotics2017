package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.JoystickCommand;
import edu.nr.robotics.OI;

public class IntakeJoystickCommand extends JoystickCommand {

	public long joystickCheckPeriod = 0; //TODO: Intake: Find period of checking for switching to joystick control
	
	public IntakeJoystickCommand() {
		super(Intake.getInstance());
	}
	
	@Override
	public void onExecute() {
		Intake.getInstance().setMotorSpeed(OI.getInstance().getIntakeValue());
	}

	@Override
	public boolean shouldSwitchToJoystick() {
		return OI.getInstance().isIntakeNonZero();
	}

	@Override
	public long getPeriodOfCheckingForSwitchToJoystick() {
		return joystickCheckPeriod;  
	}

}