package edu.nr.robotics;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	// TODO: Determine if speedMultiplier will actually be 1
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

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
}
