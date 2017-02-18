package edu.nr.robotics.subsystems.agitator;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.commandbased.DoNothingJoystickCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Agitator extends NRSubsystem {

	public static Agitator singleton;
	
	private CANTalon talon;
	
	public double motorSetpoint = 0;
	
	private Agitator() {
		if (EnabledSubsystems.AGITATOR_ENABLED) {
			talon = new CANTalon(RobotMap.AGITATOR_TALON_PORT);
			talon.changeControlMode(TalonControlMode.PercentVbus);
			talon.enableBrakeMode(true);
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
	public void setMotorSpeed(double percent) {
		motorSetpoint = percent * RobotMap.AGITATOR_DIRECTION;
		if (talon != null) {
			talon.set(motorSetpoint);
		}
	}
	
	@Override
	public void smartDashboardInfo() {
		if (talon != null) {
			if (EnabledSubsystems.AGITATOR_SMARTDASHBOARD_BASIC_ENABLED) {
				SmartDashboard.putNumber("Agitator Current", talon.getOutputCurrent());
				SmartDashboard.putString("Agitator Speed", talon.getSpeed() + " : " + getInstance().motorSetpoint);
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
		talon.set(0);
	}

}
