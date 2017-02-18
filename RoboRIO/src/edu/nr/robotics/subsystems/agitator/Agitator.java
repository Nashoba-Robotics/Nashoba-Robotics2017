package edu.nr.robotics.subsystems.agitator;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Agitator extends NRSubsystem {

	public static Agitator singleton;
	
	private CANTalon talon;
	
	public double motorSetpoint = 0;

	/**
	 * The percent voltage (-1 to 1) for the agitator to run at when turned on
	 */
	public static final double RUN_PERCENT = 0;
	public static final double REVERSE_PERCENT = -RUN_PERCENT;
	
	private Agitator() {
		if (EnabledSubsystems.AGITATOR_ENABLED) {
			talon = new CANTalon(RobotMap.AGITATOR_TALON_PORT);
			talon.changeControlMode(TalonControlMode.PercentVbus);
			talon.enableBrakeMode(false);
			talon.enable();
		}
	}
	
	public static Agitator getInstance() {
		if (singleton == null)
			init();
		return singleton;
	}
	
	public synchronized static void init() {
		if (singleton == null) {
			singleton = new Agitator();
			getInstance().setJoystickCommand(new AgitatorRunCommand());
		}
	}
	
	/**
	 * Sets motor speed of the agitator
	 * 
	 * @param speed
	 *            the agitator motor speed in percent voltage, 
	 *            from -1 to 1
	 */
	public void setMotorVoltagePercent(double percent) {
		motorSetpoint = percent;
		if (talon != null) {
			talon.set(motorSetpoint);
		} 
	}
	
	@Override
	public void smartDashboardInfo() {
		if (talon != null) {
			if (EnabledSubsystems.AGITATOR_SMARTDASHBOARD_BASIC_ENABLED) {
				SmartDashboard.putNumber("Agitator Current", talon.getOutputCurrent());
			} else if (EnabledSubsystems.AGITATOR_SMARTDASHBOARD_COMPLEX_ENABLED) {
				SmartDashboard.putNumber("Agitator Voltage", talon.getOutputVoltage());
			}
		}
	}

	@Override
	public void periodic() {
		
	}

	@Override
	public void disable() {
		setMotorVoltagePercent(0);
	}

}
