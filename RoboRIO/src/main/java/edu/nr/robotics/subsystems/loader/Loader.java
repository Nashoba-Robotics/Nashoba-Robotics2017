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

	private CANTalon lowTalon;
	private CANTalon highTalon;
	
	/**
	 * The voltage percent the motor is currently supposed to be running at
	 */
	private double lowSetpoint = 0;
	private double highSetpoint = 0;

	/**
	 * The voltage percent for the loader to run at during normal usage
	 *
	 * TODO: Loader: Get loader run speed
	 */
	public static final double LOW_RUN_VOLTAGE = 0.75;
	public static final double HIGH_RUN_VOLTAGE = 0.75;
	
	/**
	 * The voltage percent for the loader to run at while going in reverse
	 */
	public static final double LOW_REVERSE_VOLTAGE = -LOW_RUN_VOLTAGE;
	public static final double HIGH_REVERSE_VOLTAGE = -HIGH_RUN_VOLTAGE;
		
	private Loader() { 
		if (EnabledSubsystems.LOADER_ENABLED) { 
			lowTalon = new CANTalon(RobotMap.LOADER_LOW_TALON_PORT);
			lowTalon.changeControlMode(TalonControlMode.PercentVbus);
			lowTalon.enableBrakeMode(false);
			lowTalon.setInverted(true);
			lowTalon.enable();
			
			highTalon = new CANTalon(RobotMap.LOADER_HIGH_TALON_PORT);
			highTalon.changeControlMode(TalonControlMode.PercentVbus);
			highTalon.enableBrakeMode(false);
			highTalon.enable();
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
	public void setLowMotorVoltage(double percent) {
		lowSetpoint = percent;
		if (lowTalon != null) {
			lowTalon.set(lowSetpoint);
		}
	}
	
	public void setHighMotorVoltage(double percent) {
		highSetpoint = percent;
		if (highTalon != null) {
			highTalon.set(highSetpoint);
		}
	}
	
	@Override
	public void periodic() {

	}

	@Override
	public void smartDashboardInfo() {
		if (lowTalon != null && highTalon != null) {
			if(EnabledSubsystems.LOADER_SMARTDASHBOARD_BASIC_ENABLED){
				SmartDashboard.putNumber("Loader Current", lowTalon.getOutputCurrent());
				SmartDashboard.putNumber("Kicker Current", highTalon.getOutputCurrent());
			}
			if(EnabledSubsystems.LOADER_SMARTDASHBOARD_COMPLEX_ENABLED){
				SmartDashboard.putNumber("Loader Voltage", lowTalon.getOutputVoltage());
				SmartDashboard.putNumber("Kicker Voltage", highTalon.getOutputVoltage());
			}
		}
	}

	@Override
	public void disable() {
		setLowMotorVoltage(0);
		setHighMotorVoltage(0);
	}
	
}
