package edu.nr.robotics.subsystems.loader;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.commandbased.DoNothingJoystickCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Loader extends NRSubsystem {

	private static Loader singleton;

	private CANTalon talon;
	
	/**
	 * The voltage percent the motor is currently supposed to be running at
	 */
	private double setpoint = 0;

	/**
	 * The voltage percent for the loader to run at while going in reverse
	 *
	 * TODO: Loader: Get loader reverse speed
	 */
	public static final double REVERSE_VOLTAGE = 0;

	/**
	 * The voltage percent for the loader to run at during normal usage
	 *
	 * TODO: Loader: Get loader run speed
	 */
	public static final double RUN_VOLTAGE = 0;
		
	private Loader() { 
		if (EnabledSubsystems.LOADER_ENABLED) { 
			talon = new CANTalon(RobotMap.LOADER_HIGH_TALON_PORT);
			talon.changeControlMode(TalonControlMode.PercentVbus);
			talon.enableBrakeMode(false);
			talon.enable();
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
	 * Sets the motor voltage.
	 * 
	 * @param percent
	 *            the voltage that the motor should run at, on a scale from -1 to 1
	 */
	public void setMotorVoltage(double percent) {
		setpoint = percent;
		if (talon != null) {
			talon.set(setpoint);
		}
	}
	
	@Override
	public void periodic() {

	}

	@Override
	public void smartDashboardInfo() {
		if (talon != null) {
			if(EnabledSubsystems.LOADER_SMARTDASHBOARD_BASIC_ENABLED){
				SmartDashboard.putNumber("Loader Current", talon.getOutputCurrent());
			}
			if(EnabledSubsystems.LOADER_SMARTDASHBOARD_COMPLEX_ENABLED){
				SmartDashboard.putNumber("Loader Voltage", talon.getOutputVoltage());
			}
		}
	}

	@Override
	public void disable() {
		setMotorVoltage(0);
	}
	
}
