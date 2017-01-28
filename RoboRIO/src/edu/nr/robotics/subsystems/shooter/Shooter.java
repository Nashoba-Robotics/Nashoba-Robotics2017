package edu.nr.robotics.subsystems.shooter;

import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.JoystickCommand;
import edu.nr.lib.NRSubsystem;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;

public class Shooter extends NRSubsystem implements SmartDashboardSource, Periodic {

	private static Shooter singleton;

	// TODO: Initialize actual talons
	// private CANTalon ;

	private static final int ticksPerRev = 256;

	private static final double hundredMSPerMin = 0;
	private static final int nativeUnitsPerRev = 4 * ticksPerRev;

	// TODO: Create motor setpoint(s)

	public static final double turn_F = (RobotMap.MAX_SHOOTER_SPEED / hundredMSPerMin * nativeUnitsPerRev);
	public static final double turn_P = 0;
	public static final double turn_I = 0;
	public static final double turn_D = 0;

	
	private Shooter() { 
		super(/*TODO: Choose joystick command*/); 
	}
		if (EnabledSubsystems.shooterEnabled) { //TODO: Create talon(s) } }
	}

	public static Shooter getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new Shooter();
		}
	}

	/**
	 * Set the voltage ramp rate for both drive talons. Limits the rate at which
	 * the throttle will change.
	 * 
	 * @param rampRate
	 *            Maximum change in voltage, in volts / second
	 */
	public void setTalonRampRate(double rampRate) {
		// TODO: Set voltage ramp rates of talons
	}

	/**
	 * Sets motor speed of shooter
	 * 
	 * @param speed
	 *            the shooter motor speed, from -1 to 1
	 */
	public void setMotorSpeed(double speed) {
		//TODO: Set actual motor speeds
	}
	
	/**
	 * Gets whether the PIDs are enabled or not. If both are enabled, then
	 * returns true, otherwise returns false
	 * 
	 * @return whether the PIDs are enabled
	 */
	public boolean getPIDEnabled() {
		//TODO: Check if talons are in speed mode
		return false;
	}

	/**
	 * Enables or disables both left and right PIDs. Disabling also resets the
	 * integral term and the previous error of the PID, and sets the output to
	 * zero
	 * 
	 * Doesn't do anything if they are already that state.
	 * 
	 * @param enabled
	 *            whether to enable (true) or disable (false)
	 */
	public void setPIDEnabled(boolean enabled) {
		//Set PIDEnabled for shooter talon(s)
	}
	
	/**
	 * Resets both the left and right encoders
	 */
	public void resetEncoders() {
		//Set talon position(s) to 0
	}
	
	/**
	 * Resets talons
	 */
	public void resetTalons() {
		//ClearIAccum of talon(s)
	}
	
	@Override
	public void periodic() {

	}

	@Override
	public void smartDashboardInfo() {

	}

	@Override
	public void disable() {

	}

}
