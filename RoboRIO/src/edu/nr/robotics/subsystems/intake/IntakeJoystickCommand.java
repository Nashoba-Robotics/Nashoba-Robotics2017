package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;

public class IntakeJoystickCommand extends JoystickCommand {
	
	public IntakeJoystickCommand() {
		super(Intake.getInstance());
	}
	
	@Override
	public void onExecute() {
		if (OI.getInstance().isShooterOn()) {
			Intake.getInstance().setMotorSpeed(OI.getInstance().getIntakeValue());
		}
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
