package edu.nr.robotics;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// TODO: Get actual numbers
	public static final int joystickLeftPort = 0;
	public static final int joystickRightPort = 1;

	// Talon ports
	public static final int talonLF = 0;
	public static final int talonRF = 2;
	public static final int talonLB = 1;
	public static final int talonRB = 3;

	public static final double WHEEL_DIAMETER = 3.82; //inches
	
	public static final double MAX_RPS = 13.33; 
	public static final double MAX_ACC = 31.53;//RPS per second
	public static final double MAX_JERK = 813.0; //No longer a total guess
}
