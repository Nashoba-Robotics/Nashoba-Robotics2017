package edu.nr.robotics;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	public double driveSpeedMultiplier = 1;

	private static OI singleton;

	private Joystick driveLeft;
	private Joystick driveRight;

	private OI() {
		driveLeft = new Joystick(RobotMap.STICK_LEFT);
		driveRight = new Joystick(RobotMap.STICK_RIGHT);

		initDriveLeft();
		initDriveRight();
	}

	public void initDriveLeft() {

	}

	public void initDriveRight() {

	}

	public static OI getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new OI();
		}
	}

	public Joystick getLeftDriveStick() {
		return driveLeft;
	}

	public Joystick getRightDriveStick() {
		return driveRight;
	}

}
