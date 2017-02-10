package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRMath;
import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.lib.interfaces.GyroCorrection;
import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;

public class DriveJoystickCommand extends JoystickCommand {

	GyroCorrection gyroCorrection;

	public DriveJoystickCommand() {
		super(Drive.getInstance());
	}

	@Override
	public void onExecute() {
		if (RobotMap.driveMode == Drive.DriveMode.arcadeDrive) {
			
			//Get the joystick values
			double moveValue = OI.getInstance().getArcadeMoveValue();
			double rotateValue = OI.getInstance().getArcadeTurnValue();
			
			//Square the inputs (while preserving the sign) to increase fine control while permitting full power
			moveValue = NRMath.squareWithSign(moveValue);
			rotateValue = NRMath.squareWithSign(rotateValue);
			
			//Make the gyro guide us when we're going straight, 
			//otherwise reset the gyroscrope and use the joystick turn value
			if (Math.abs(rotateValue) < 0.05) {
				if (Math.abs(moveValue) > .1) {
					rotateValue = gyroCorrection.getTurnValue();
				} else {
					gyroCorrection.clearInitialValue();
				}
			} else {
				gyroCorrection.clearInitialValue();
			}
			Drive.getInstance().arcadeDrive(moveValue * OI.getInstance().driveSpeedMultiplier, rotateValue * OI.getInstance().driveSpeedMultiplier);
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
		if(RobotMap.driveMode == Drive.DriveMode.arcadeDrive) {
			return OI.getInstance().getArcadeMoveValue() != 0 || OI.getInstance().getArcadeTurnValue() != 0;
		} else {
			return OI.getInstance().getTankLeftValue() != 0 || OI.getInstance().getTankRightValue() != 0;
		}
	}

	@Override
	public long getPeriodOfCheckingForSwitchToJoystick() {
		return 100; // TODO: Drive: Find the best period for checking DriveJoystickCommand
	}

}
