package edu.nr.robotics;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// TODO: Generic: Get actual talon ports
	// These are the talon ports
	public static final int DRIVE_LEFT_F_TALON_PORT = -1;
	public static final int DRIVE_LEFT_B_TALON_PORT = -1;
	public static final int DRIVE_RIGHT_F_TALON_PORT = -1;
	public static final int DRIVE_RIGHT_B_TALON_PORT = -1;
	public static final int SHOOTER_TALON_PORT = -1;
	public static final int CLIMBER_TALON_PORT = -1;
	public static final int TURRET_TALON_PORT = -1;
	public static final int LOADER_LOW_TALON_PORT = -1;
	public static final int LOADER_HIGH_TALON_PORT = -1;
	public static final int AGITATOR_TALON_PORT = -1;
	public static final int INTAKE_LOW_TALON_PORT = -1;
	public static final int INTAKE_HIGH_TALON_PORT = -1;
	public static final int HOOD_TALON_PORT = -1;	
	
	//TODO: Generic: Get actual PCM ports
	public static final int INTAKE_ARM_PCM_PORT = 0;
	public static final int INTAKE_ARM_FORWARD = 0;
	public static final int INTAKE_ARM_REVERSE = 0;

	public static final int INTAKE_SLIDE_PCM_PORT = 0;
	public static final int INTAKE_SLIDE_FORWARD = 0;
	public static final int INTAKE_SLIDE_REVERSE = 0;

	public static final int GEAR_MOVER_PCM_PORT = 0;
	public static final int GEAR_MOVER_FORWARD = 0;
	public static final int GEAR_MOVER_REVERSE = 0;
	
	public static final int GEAR_GET_POSITION_PCM_PORT = 0;
	public static final int GEAR_GET_POSITION_FORWARD = 0;
	public static final int GEAR_GET_POSITION_REVERSE = 0;
	
	public static final int DRIVE_GEAR_SWITCHER_PCM_PORT = 0;
	public static final int DRIVE_GEAR_SWITCHER_FORWARD_CHANNEL = 0;
	public static final int DRIVE_GEAR_SWITCHER_REVERSE_CHANNEL = 0;
	
	/**
	 * Offsets of camera and turret used for shooting on move calculations
	 * 
	 * Turret offsets are how far the turret is from the center of the robot
	 * Camera offsets are offset from turret when turret is at 0 degrees (facing driving direction of robot)
	 */
	public static final double Y_CAMERA_OFFSET = 0; //TODO: Turret: Get y camera offset
	public static final double X_CAMERA_OFFSET = 0;
	public static final double X_TURRET_OFFSET = 5.465;
	public static final double Y_TURRET_OFFSET = -7;


}
