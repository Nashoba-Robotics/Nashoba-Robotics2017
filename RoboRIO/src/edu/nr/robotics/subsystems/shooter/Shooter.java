package edu.nr.robotics.subsystems.shooter;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.DoNothingJoystickCommand;
import edu.nr.lib.JoystickCommand;
import edu.nr.lib.NRSubsystem;
import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends NRSubsystem {

	private static Shooter singleton;

	private CANTalon talon;
	
	public double motorSetpoint = 0;

	private static final int TICKS_PER_REV = 256;

	private static final double HUNDRED_MS_PER_MIN = 600;
	private static final int NATIVE_UNITS_PER_REV = 4 * TICKS_PER_REV;

	//TODO: Make final once tested using SmartDashboard
	public static double F = (RobotMap.MAX_SHOOTER_SPEED / HUNDRED_MS_PER_MIN * NATIVE_UNITS_PER_REV);
	public static double P = 0;
	public static double I = 0;
	public static double D = 0;

	
	private Shooter() { 
		if (EnabledSubsystems.SHOOTER_ENABLED) { 
			talon = new CANTalon(RobotMap.SHOOTER_TALON);
			talon.changeControlMode(TalonControlMode.PercentVbus);
			talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			talon.setF(F);
			talon.setP(P);
			talon.setI(I);
			talon.setD(D);
			talon.configEncoderCodesPerRev(TICKS_PER_REV);
			talon.enableBrakeMode(false);
			talon.setEncPosition(0);
			talon.reverseSensor(false);
			talon.enable();
		}
	}

	public static Shooter getInstance() {
		init();
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new Shooter();
			singleton.setJoystickCommand(new DoNothingJoystickCommand(singleton));
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

	}

	/**
	 * Sends data to SmartDashboard
	 */
	@Override
	public void smartDashboardInfo() {
		if (talon != null) {
			SmartDashboard.putNumber("Shooter Current", talon.getOutputCurrent());
			SmartDashboard.putNumber("Shooter Voltage", talon.getOutputVoltage());
			SmartDashboard.putString("Shooter Speed", talon.getSpeed() + " : " + getInstance().motorSetpoint);
		}
	}

	/**
	 * What subsystem does upon robot being disabled
	 */
	@Override
	public void disable() {
		setMotorSpeed(0);
	}

	public void setPID(double P, double I, double D, double F) {
		if(talon != null) {
			talon.setPID(P, I, D);
			talon.setF(F);
		}
	}

}
