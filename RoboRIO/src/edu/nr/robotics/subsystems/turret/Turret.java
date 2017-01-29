package edu.nr.robotics.subsystems.turret;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.DoNothingJoystickCommand;
import edu.nr.lib.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turret extends NRSubsystem {

	public static Turret singleton;

	private CANTalon talon;
	
	public double motorSetpoint = 0;
	
	private static final int TICKS_PER_REV = 256;

	private static final double HUNDRED_MS_PER_MIN = 600;
	private static final int NATIVE_UNITS_PER_REV = 4 * TICKS_PER_REV;

	public static final double F = (RobotMap.MAX_TURRET_SPEED / HUNDRED_MS_PER_MIN * NATIVE_UNITS_PER_REV);
	public static final double P = 0;
	public static final double I = 0;
	public static final double D = 0;
	
	public static final int FORWARD_POSITION = 0; //TODO
	public static final int REVERSE_POSITION = 0; //TODO

	
	private Turret() { 
		if (EnabledSubsystems.TURRET_ENABLED) { 
			talon = new CANTalon(RobotMap.TURRET_TALON);
			
			talon.changeControlMode(TalonControlMode.PercentVbus);
			talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			talon.setF(F);
			talon.setP(P);
			talon.setI(I);
			talon.setD(D);
			talon.configEncoderCodesPerRev(TICKS_PER_REV);
			talon.enableBrakeMode(true);
			talon.setEncPosition(0);
			talon.reverseSensor(false);
			talon.enable();
		}
	}

	public static Turret getInstance() {
		init();
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new Turret();
			singleton.setJoystickCommand(new DoNothingJoystickCommand(singleton));
		}
	}

	/**
	 * Sets motor speed of turret
	 * 
	 * @param speed
	 *            the turret motor speed, 
	 *            
	 *            If the talon mode is Speed, from -MAX_RPM to MAX_RPM
	 *            If the talon mode is PercentVBus from -1 to 1
	 */
	public void setMotorSpeed(double speed) {
		motorSetpoint = speed;
		if (talon != null) {
			talon.set(speed);
		}
	}
	
	/**
	 * Function that is periodically called once the Shooter class is initialized
	 */
	@Override
	public void periodic() {
		if(talon != null) {
			if(talon.isFwdLimitSwitchClosed()) {
				talon.setEncPosition(FORWARD_POSITION);
			} else if(talon.isRevLimitSwitchClosed()) {
				talon.setEncPosition(REVERSE_POSITION);
			} 
		}

	}

	/**
	 * Sends data to SmartDashboard
	 */
	@Override
	public void smartDashboardInfo() {
		if (talon != null) {
			SmartDashboard.putNumber("Turret Current", talon.getOutputCurrent());
			SmartDashboard.putNumber("Turret Voltage", talon.getOutputVoltage());
			SmartDashboard.putString("Turret Speed", talon.getSpeed() + " : " + getInstance().motorSetpoint);
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
