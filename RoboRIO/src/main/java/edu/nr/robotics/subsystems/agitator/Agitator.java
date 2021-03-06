package edu.nr.robotics.subsystems.agitator;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Agitator extends NRSubsystem {

	private static Agitator singleton;
	
	private CANTalon talon;
	
	public double motorSetpoint = 0;

	/**
	 * The percent voltage (-1 to 1) for the agitator to run at when turned on
	 * TODO: Agitator: Get run speed
	 */
	public static final double HIGH_RUN_PERCENT = .75;
	
	/**
	 * The percent voltage (-1 to 1) for the agitator to run at when shooter is off
	 * TODO: Agitator: Get slow run speed
	 */
	public static final double LOW_RUN_PERCENT = .75;
	
	/**
	 * The percent voltage (-1 to 1) for the agitator to run at when reversing
	 */
	public static final double REVERSE_PERCENT = -HIGH_RUN_PERCENT;
	
	private Agitator() {
		if (EnabledSubsystems.AGITATOR_ENABLED) {
			talon = new CANTalon(RobotMap.AGITATOR_TALON_PORT);
			talon.changeControlMode(TalonControlMode.PercentVbus);
			talon.enableBrakeMode(false);
			talon.setInverted(true);
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
	 * Sets the motor voltage.
	 * 
	 * @param percent the voltage that the motor should run at, on a scale from -1 to 1
	 */
	public void setMotorVoltage(double percent) {
		motorSetpoint = percent;
		if (talon != null) {
			if(OI.getInstance().isAgitatorOn()) {
				talon.set(motorSetpoint);
			} else {
				talon.set(0);
			}
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
		setMotorVoltage(0);
	}

}
