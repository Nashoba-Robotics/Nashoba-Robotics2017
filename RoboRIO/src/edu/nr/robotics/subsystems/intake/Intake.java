package edu.nr.robotics.subsystems.intake;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.DoNothingJoystickCommand;
import edu.nr.lib.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends NRSubsystem {

	public static Intake singleton;

	private CANTalon lowTalon, highTalon;
	
	public double lowMotorSetpoint = 0;
	public double highMotorSetpoint = 0;
	
	private Intake() { 
		if (EnabledSubsystems.INTAKE_ENABLED) { 
			lowTalon = new CANTalon(RobotMap.INTAKE_LOW_TALON);
			lowTalon.changeControlMode(TalonControlMode.PercentVbus);
			lowTalon.enableBrakeMode(true);
			lowTalon.enable();
		
			highTalon = new CANTalon(RobotMap.INTAKE_HIGH_TALON);
			highTalon.changeControlMode(TalonControlMode.PercentVbus);
			highTalon.enableBrakeMode(true);
			highTalon.enable();
		}
	}

	public static Intake getInstance() {
		init();
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new Intake();
			singleton.setJoystickCommand(new DoNothingJoystickCommand(singleton));
		}
	}

	/**
	 * Sets motor speed of  intake
	 * 
	 * @param speed
	 *            the intake motor speed, 
	 *            
	 *            from -1 to 1
	 */
	public void setMotorSpeed(double speed) {
		lowMotorSetpoint = speed * RobotMap.LOW_INTAKE_DIRECTION;
		highMotorSetpoint = speed * RobotMap.HIGH_INTAKE_DIRECTION;
		if (lowTalon != null && highTalon != null) {
			lowTalon.set(lowMotorSetpoint);
			highTalon.set(highMotorSetpoint);
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
		if (lowTalon != null && highTalon != null) {
			SmartDashboard.putNumber("Low Intake Current", lowTalon.getOutputCurrent());
			SmartDashboard.putNumber("High Intake Current", highTalon.getOutputCurrent());
			SmartDashboard.putNumber("Low Intake Voltage", lowTalon.getOutputVoltage());
			SmartDashboard.putNumber("High Intake Voltage", highTalon.getOutputVoltage());
			SmartDashboard.putString("Low Intake Speed", lowTalon.getSpeed() + " : " + getInstance().lowMotorSetpoint);
			SmartDashboard.putString("High Intake Speed", highTalon.getSpeed() + " : " + getInstance().highMotorSetpoint);
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
