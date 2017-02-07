package edu.nr.robotics;

import edu.nr.robotics.subsystems.drive.Drive;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// TODO: Generic: Get actual talon ports
	// These are the talon ports
	public static final int TALON_LEFT_F = -1;
	public static final int TALON_LEFT_B = -1;
	public static final int TALON_RIGHT_F = -1;
	public static final int TALON_RIGHT_B = -1;
	public static final int SHOOTER_TALON = -1;
	public static final int CLIMBER_TALON = -1;
	public static final int TURRET_TALON = -1;
	public static final int LOADER_TALON = -1;
	public static final int INTAKE_LOW_TALON = -1;
	public static final int INTAKE_HIGH_TALON = -1;
	public static final int HOOD_TALON = -1;

	// TODO: Generic: Get actual Joystick ports
	public static final int STICK_LEFT = -1;
	public static final int STICK_RIGHT = -1;
	
	// TODO: Drive: Get actual max speed
	public static final double MAX_DRIVE_SPEED = 0; //In feet per second
	
	// TODO: Generic: Get actual subsystem max speeds
	public static final double MAX_SHOOTER_SPEED = 0; //In rpm
	public static final double MAX_CLIMBER_SPEED = 0; //In rpm
	public static final double MAX_TURRET_SPEED = 0; //In rpm
	public static final double MAX_LOW_INTAKE_SPEED = 0; //In rpm
	public static final double MAX_HIGH_INTAKE_SPEED = 0; //In rpm
	public static final double MAX_LOADER_SPEED = 0; //In rpm
	public static final double MAX_HOOD_SPEED = 0; //In rpm
	
	public static final double MAX_HOOD_ACCELERATION = 0; //In rpm/s
	public static final double MAX_TURRET_ACCELERATION = 0; //In rpm/s 

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
	
	public static final Drive.driveMode driveMode = Drive.driveMode.arcadeDrive;
	
	//TODO: Intake: Get actual PCM ports
	public static final int INTAKE_ARM_PNEUMATIC = 0;
	public static final int INTAKE_ARM_FORWARD = 0;
	public static final int INTAKE_ARM_REVERSE = 0;
	
	public static final int GEAR_MOVER_PNEUMATIC = 0;
	public static final int GEAR_MOVER_FORWARD = 0;
	public static final int GEAR_MOVER_REVERSE = 0;
	
	public static final int TICKS_PER_REV = 256;
	public static final double HUNDRED_MS_PER_MIN = 600;
	public static final int NATIVE_UNITS_PER_REV = 4 * TICKS_PER_REV;
}
