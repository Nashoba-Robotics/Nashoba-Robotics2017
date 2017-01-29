package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.JoystickCommand;
import edu.nr.lib.NRMath;
import edu.nr.lib.interfaces.GyroCorrection;
import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.Joystick;

public class DriveJoystickCommand extends JoystickCommand {

	Joystick leftJoystick;
	Joystick rightJoystick;

	GyroCorrection gyroCorrection;

	public DriveJoystickCommand(Joystick leftJoystick, Joystick rightJoystick) {
		super(Drive.getInstance());
		this.leftJoystick = leftJoystick;
		this.rightJoystick = rightJoystick;
	}

	@Override
	public void onExecute() {
		if (RobotMap.driveMode == Drive.driveMode.arcadeDrive) {
			double moveValue = OI.getInstance().getArcadeMoveValue();
			double rotateAdjustValue = OI.getInstance().getTurnAdjust();
			double rotateValue = OI.getInstance().getArcadeTurnValue() * rotateAdjustValue;
			moveValue = NRMath.squareWithSign(moveValue);
			rotateValue = NRMath.squareWithSign(rotateValue);
			if (Math.abs(rotateValue) < 0.05) {
				if (Math.abs(moveValue) > .1) {
					rotateValue = gyroCorrection.getTurnValue();
				} else {
					gyroCorrection.clearInitialValue();
				}
			} else {
				gyroCorrection.clearInitialValue();
			}
			Drive.getInstance().arcadeDrive(moveValue, rotateValue, true);
		} else {
			// Get values of the joysticks
			double left = OI.getInstance().getTankLeftValue();
			double right = OI.getInstance().getTankRightValue();
			// Do the math for turning
			if (Math.abs(left - right) < .25) {
				left = (Math.abs(left) + Math.abs(right)) / 2 * Math.signum(left);
				right = (Math.abs(left) + Math.abs(right)) / 2 * Math.signum(right);
			}
			// Cube the inputs (while preserving the sign) to increase fine
			// control while permitting full power
			right = right * right * right;
			left = left * left * left;
			Drive.getInstance().tankDrive(OI.getInstance().driveSpeedMultiplier * left,
					-OI.getInstance().driveSpeedMultiplier * right);
		}
	}

	@Override
	public boolean shouldSwitchToJoystick() {
		return false; // TODO: Should go if the joysticks are outside the
						// threshold range.
	}

	@Override
	public long getPeriodOfCheckingForSwitchToJoystick() {
		return 100; // TODO find the best period
	}

}
