package edu.nr.robotics;

import edu.nr.lib.units.Distance;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// These are the talon ports
	public static final int DRIVE_LEFT_F_TALON_PORT = 15;
	public static final int DRIVE_LEFT_B_TALON_PORT = 14;
	public static final int DRIVE_RIGHT_F_TALON_PORT = 12;
	public static final int DRIVE_RIGHT_B_TALON_PORT = 13;
	public static final int SHOOTER_A_TALON_PORT = 3;
	public static final int SHOOTER_B_TALON_PORT = 1;
	public static final int TURRET_TALON_PORT = 11;
	public static final int LOADER_LOW_TALON_PORT = 5;
	public static final int LOADER_HIGH_TALON_PORT = 4;
	public static final int AGITATOR_TALON_PORT = 10;
	public static final int INTAKE_LOW_TALON_PORT = 9;
	public static final int INTAKE_HIGH_TALON_PORT = 0;
	public static final int HOOD_TALON_PORT = 2;	
	
	//Forwards are even, reverses are odd
	public static final int INTAKE_ARM_PCM_PORT = 0;
	public static final int INTAKE_ARM_FORWARD = 6;
	public static final int INTAKE_ARM_REVERSE = 7;

	public static final int INTAKE_SLIDE_PCM_PORT = 0;
	public static final int INTAKE_SLIDE_FORWARD = 4;
	public static final int INTAKE_SLIDE_REVERSE = 5;

	public static final int GEAR_MOVER_PCM_PORT = 0;
	public static final int GEAR_MOVER_FORWARD = 0;
	public static final int GEAR_MOVER_REVERSE = 1;
	
	public static final int GEAR_GET_POSITION_PCM_PORT = 0;
	public static final int GEAR_GET_POSITION_FORWARD = 2;
	public static final int GEAR_GET_POSITION_REVERSE = 3;
	
	public static final int DRIVE_GEAR_SWITCHER_PCM_PORT = 1;
	public static final int DRIVE_GEAR_SWITCHER_FORWARD_CHANNEL = 0;
	public static final int DRIVE_GEAR_SWITCHER_REVERSE_CHANNEL = 1;
	
	/**
	 * Offsets of camera and turret used for shooting on move calculations
	 * 
	 * Turret offsets are how far the turret is from the center of the robot
	 * Camera offsets are offset from turret when turret is at 0 degrees (facing driving direction of robot)
	 */
	public static final Distance Y_CAMERA_OFFSET = new Distance(6.75, Distance.Unit.INCH);
	public static final Distance X_CAMERA_OFFSET = Distance.ZERO;
	public static final Distance X_TURRET_OFFSET = new Distance(5.465, Distance.Unit.INCH);
	public static final Distance Y_TURRET_OFFSET = new Distance(7, Distance.Unit.INCH);
	
	/**
	 * The distance from the back of the back bumper to the front of the gear mechanism.
	 * This is used in autonomous modes when the bumper is against the wall.
	 * 
	 * TODO: RobotMap: Get BUMPER_TO_GEAR_DIST
	 */
	public static final Distance BUMPER_TO_GEAR_DIST = Distance.ZERO;

}
