package edu.nr.robotics.subsystems.intake;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.DoNothingJoystickCommand;
import edu.nr.lib.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.climber.Climber;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends NRSubsystem {

	public static Intake singleton;

	private CANTalon lowTalon, highTalon;
	
	public double lowMotorSetpoint = 0;
	public double highMotorSetpoint = 0;
	
	private static final int TICKS_PER_REV = 256;

	private static final double HUNDRED_MS_PER_MIN = 600;
	private static final int NATIVE_UNITS_PER_REV = 4 * TICKS_PER_REV;

	//TODO: Make final once tested using SmartDashboard
	public static double F_LOW = (RobotMap.MAX_LOW_INTAKE_SPEED / HUNDRED_MS_PER_MIN * NATIVE_UNITS_PER_REV);
	public static double P_LOW = 0;
	public static double I_LOW = 0;
	public static double D_LOW = 0;
	
	public static double F_HIGH = (RobotMap.MAX_HIGH_INTAKE_SPEED / HUNDRED_MS_PER_MIN * NATIVE_UNITS_PER_REV);
	public static double P_HIGH = 0;
	public static double I_HIGH = 0;
	public static double D_HIGH = 0;

	
	private Intake() { 
		if (EnabledSubsystems.INTAKE_ENABLED) { 
			lowTalon = new CANTalon(RobotMap.INTAKE_LOW_TALON);
			lowTalon.changeControlMode(TalonControlMode.PercentVbus);
			lowTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			lowTalon.setF(F_LOW);
			lowTalon.setP(P_LOW);
			lowTalon.setI(I_LOW);
			lowTalon.setD(D_LOW);
			lowTalon.configEncoderCodesPerRev(TICKS_PER_REV);
			lowTalon.enableBrakeMode(true);
			lowTalon.setEncPosition(0);
			lowTalon.reverseSensor(false);
			lowTalon.enable();
		
			highTalon = new CANTalon(RobotMap.INTAKE_HIGH_TALON);
			highTalon.changeControlMode(TalonControlMode.PercentVbus);
			highTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			highTalon.setF(F_HIGH);
			highTalon.setP(P_HIGH);
			highTalon.setI(I_HIGH);
			highTalon.setD(D_HIGH);
			highTalon.configEncoderCodesPerRev(TICKS_PER_REV);
			highTalon.enableBrakeMode(true);
			highTalon.setEncPosition(0);
			highTalon.reverseSensor(false);
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
	 *            the faster intake motor speed, 
	 *            
	 *            If the talon mode is Speed, from -MAX_RPM to MAX_RPM
	 *            If the talon mode is PercentVBus from -1 to 1
	 */
	public void setMotorSpeed(double speed) {
		lowMotorSetpoint = speed;
		highMotorSetpoint = speed;
		if (lowTalon != null && highTalon != null) {
			lowTalon.set(speed);
			highTalon.set(speed);
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

	public void setPID(double PLow, double ILow, double DLow, double FLow, double PHigh, double IHigh, double DHigh, double FHigh) {
		if(lowTalon != null && highTalon != null) {
			lowTalon.setPID(PLow, ILow, DLow);
			lowTalon.setF(FLow);
			highTalon.setPID(PHigh, IHigh, DHigh);
			highTalon.setF(FHigh);
		}
	}
	
}
