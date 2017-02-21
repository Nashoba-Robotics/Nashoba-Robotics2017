package edu.nr.robotics.subsystems;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;
import edu.nr.robotics.Robot;

public class DriveJoystickCommand extends JoystickCommand {

	
	public DriveJoystickCommand() {
		super(Drive.getInstance());
	}

	@Override
	protected void onStart() {
	}

	@Override
	protected void onExecute() {
		double[] motorSpeedValues = OI.getInstance().getMotorSpeedValues();
		switch (Robot.getInstance().joystickChooser.getSelected()) {
		case off:
			break;
		case on:
			Drive.getInstance().arcadeDrive(motorSpeedValues[0], motorSpeedValues[1], false);
		}
	}

	@Override
	protected void onEnd() {
	}

	@Override
	protected boolean shouldSwitchToJoystick() {
		return !Drive.getInstance().isProfilerEnabled();
	}

	@Override
	protected long getPeriodOfCheckingForSwitchToJoystick() {
		return 100;
	}
}
