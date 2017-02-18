package edu.nr.robotics.subsystems.loader;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.commandbased.DoNothingJoystickCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Loader extends NRSubsystem {

	public static Loader singleton;

	private CANTalon talonLow;
	private CANTalon talonHigh;
	
	public double motorSetpoint = 0;

	/**
	 * The speed in rotations per minute for the loader to run at while going in reverse
	 *
	 * TODO: Get loader reverse speed
	 */
	public static final double REVERSE_SPEED = 0;

	/**
	 * The speed in rotations per minute for the loader to run at during normal usage
	 *
	 * TODO: Get loader run speed
	 */
	public static final double RUN_SPEED = 0;
		
	private Loader() { 
		if (EnabledSubsystems.LOADER_ENABLED) { 		
			talonLow = new CANTalon(RobotMap.LOADER_LOW_TALON_PORT);
			talonLow.changeControlMode(TalonControlMode.PercentVbus);
			talonLow.enableBrakeMode(false);
			talonLow.enable();
			
			talonHigh = new CANTalon(RobotMap.LOADER_HIGH_TALON_PORT);
			talonHigh.changeControlMode(TalonControlMode.PercentVbus);
			talonHigh.enableBrakeMode(false);
			talonHigh.enable();
		}
	}

	public static Loader getInstance() {
		if (singleton == null)
			init();
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new Loader();
			singleton.setJoystickCommand(new DoNothingJoystickCommand(singleton));
		}
	}

	/**
	 * Sets motor speed of the loader
	 * 
	 * @param speed
	 *            the loader motor speed, from -1 to 1
	 */
	public void setMotorSpeed(double speed) {
		motorSetpoint = speed;
		if (talonLow != null && talonHigh != null) {
			talonLow.set(motorSetpoint);
			talonHigh.set(motorSetpoint);
		}
	}
	
	/**
	 * Function that is periodically called once the subsystem class is initialized
	 */
	@Override
	public void periodic() {

	}

	/**
	 * Sends data to SmartDashboard
	 */
	@Override
	public void smartDashboardInfo() {
		if (talonLow != null && talonHigh != null) {
			if(EnabledSubsystems.LOADER_SMARTDASHBOARD_BASIC_ENABLED){
				SmartDashboard.putNumber("Loader Low Motor Current", talonLow.getOutputCurrent());
				SmartDashboard.putNumber("Loader High Motor Current", talonHigh.getOutputCurrent());
			}
			if(EnabledSubsystems.LOADER_SMARTDASHBOARD_COMPLEX_ENABLED){
				SmartDashboard.putNumber("Loader Low Motor Voltage", talonLow.getOutputVoltage());
				SmartDashboard.putNumber("Loader High Motor Voltage", talonHigh.getOutputVoltage());
			}
		}
	}

	/**
	 * What subsystem does upon robot being disabled
	 */
	@Override
	public void disable() {
		setMotorSpeed(0);
	}
	
}
