package edu.nr.robotics.subsystems.shooter;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.DoNothingJoystickCommand;
import edu.nr.lib.JoystickCommand;
import edu.nr.lib.NRSubsystem;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.drive.DriveJoystickCommand;

public class Shooter extends NRSubsystem {

	private static Shooter singleton;

	// TODO: Initialize actual talons
	private CANTalon talon;

	private static final int ticksPerRev = 256;

	private static final double hundredMSPerMin = 0;
	private static final int nativeUnitsPerRev = 4 * ticksPerRev;

	// TODO: Create motor setpoint(s)

	public static final double F = (RobotMap.MAX_SHOOTER_SPEED / hundredMSPerMin * nativeUnitsPerRev);
	public static final double P = 0;
	public static final double I = 0;
	public static final double D = 0;

	
	private Shooter() { 
		if (EnabledSubsystems.shooterEnabled) { /*TODO: Create talon(s)*/ }
	}

	public static Shooter getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new Shooter();
			getInstance().setJoystickCommand(new DoNothingJoystickCommand(getInstance()));
		}
	}

	/**
	 * Sets motor speed of shooter
	 * 
	 * @param speed
	 *            the shooter motor speed, 
	 *            
	 *            If the talon mode is Speed, from -MAX_RPM to MAX_RPM
	 *            If the talon mode is PercentVBus from -1 to 1
	 */
	public void setMotorSpeed(double speed) {
		talon.set(speed);
	}
	
	@Override
	public void periodic() {

	}

	@Override
	public void smartDashboardInfo() {

	}

	@Override
	public void disable() {
		setMotorSpeed(0);
	}

}
