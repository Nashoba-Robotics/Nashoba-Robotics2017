package edu.nr.robotics;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveJoystickCommand;

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
	public static final int LOADER_TALON_PORT = -1;
	public static final int INTAKE_LOW_TALON_PORT = -1;
	public static final int INTAKE_HIGH_TALON_PORT = -1;
	public static final int HOOD_TALON_PORT = -1;

	// TODO: OI: Get actual Joystick ports
	public static final int STICK_LEFT = -1;
	public static final int STICK_RIGHT = -1;
	public static final int STICK_OPERATOR_LEFT = 0;
	public static final int STICK_OPERATOR_RIGHT = 0;
	
	// TODO: Drive: Get actual max speed
	/**
	 * The max driving speed of the robot, in feet per second
	 */
	public static final double MAX_DRIVE_SPEED = 0;
	
	// TODO: Generic: Get actual subsystem max speeds
	/**
	 * The max speed of the shooter, in rotations per minute
	 */
	public static final double MAX_SHOOTER_SPEED = 0;
	
	/**
	 * The max speed of the turret, in rotations per minute
	 */
	public static final double MAX_TURRET_SPEED = 0;
	
	/**
	 * The max speed of the lower intake motor, in rotations per minute
	 */
	public static final double MAX_LOW_INTAKE_SPEED = 0;
	
	/**
	 * The max speed of the higher intake motor, in rotations per minute
	 */
	public static final double MAX_HIGH_INTAKE_SPEED = 0;
	
	/**
	 * The max speed of the loader, in rotations per minute
	 */
	public static final double MAX_LOADER_SPEED = 0;
	
	/**
	 * The max speed of the hood, in rotations per minute
	 */
	public static final double MAX_HOOD_SPEED = 0;
	
	/**
	 * The max acceleration of the hood, in rotations per minute per second
	 */
	public static final double MAX_HOOD_ACCELERATION = 0;
	
	/**
	 * The max acceleration of the turret, in rotations per minute per second
	 */
	public static final double MAX_TURRET_ACCELERATION = 0;

	// These are multipliers for each subsystem that allow for wiring changes
	// Adding a negative will switch the motor direction
	public static final int LEFT_DRIVE_DIRECTION = 1;
	public static final int RIGHT_DRIVE_DIRECTION = 1;
	public static final int SHOOTER_DIRECTION = 1;
	public static final int HOOD_DIRECTION = 1;
	public static final int HIGH_INTAKE_DIRECTION = 1;
	public static final int LOW_INTAKE_DIRECTION = 1;
	public static final int LOADER_DIRECTION = 1;
	public static final int TURRET_DIRECTION = 1;
	
	
	/**
	 * What {@link Drive#DriveMode} for the {@link DriveJoystickCommand} to use.
	 */
	public static final Drive.DriveMode driveMode = Drive.DriveMode.arcadeDrive;
	
	//TODO: Intake: Get actual PCM ports
	public static final int INTAKE_ARM_PNEUMATIC = 0;
	public static final int INTAKE_ARM_FORWARD = 0;
	public static final int INTAKE_ARM_REVERSE = 0;
	
	public static final int GEAR_MOVER_PNEUMATIC = 0;
	public static final int GEAR_MOVER_FORWARD = 0;
	public static final int GEAR_MOVER_REVERSE = 0;
	
	/**
	 * The number of hundred of millisecond periods per minute
	 * 
	 * This is used to calculate the feedforward value for a velocity feedback PID that is running on a CANTalon.
	 */
	public static final double HUNDRED_MS_PER_MIN = 600;

	/**
	 * The speed in rotations per minute for the intake to run at during normal usage
	 */
	public static final double INTAKE_RUN_SPEED = 0;
	
	/**
	 * The speed in rotations per minute for the loader to run at during normal usage
	 */
	public static final double LOADER_RUN_SPEED = 0;

}
