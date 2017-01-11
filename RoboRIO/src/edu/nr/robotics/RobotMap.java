package edu.nr.robotics;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// TODO: Get actual talon ports
	// These are the talon ports
	public static final int TALON_LEFT_A = 0;
	public static final int TALON_LEFT_B = 0;
	public static final int TALON_RIGHT_A = 0;
	public static final int TALON_RIGHT_B = 0;

	// TODO: Get actual Joystick ports
	public static final int STICK_LEFT = 0;
	public static final int STICK_RIGHT = 0;
	
	// TODO: Get actual max speed
	public static final double MAX_SPEED = 0;

	// These are multipliers for each subsystem that allow for wiring changes
	// Adding a negative will switch the motor direction
	public static final int LEFT_DRIVE_DIRECTION = 1;
	public static final int RIGHT_DRIVE_DIRECTION = 1;
}
