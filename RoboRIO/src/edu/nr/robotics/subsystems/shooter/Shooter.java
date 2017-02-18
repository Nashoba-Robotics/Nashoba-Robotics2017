package edu.nr.robotics.subsystems.shooter;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.DoNothingJoystickCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends NRSubsystem {

	private static Shooter singleton;

	private CANTalon talon;
	
	public double motorSetpoint = 0;

	//TODO: Shooter: Find FPID values
	public static double F = (Shooter.MAX_SHOOTER_SPEED / Units.HUNDRED_MS_PER_MIN * Units.MAGNETIC_NATIVE_UNITS_PER_REV);
	public static double P = 0;
	public static double I = 0;
	public static double D = 0;

	private boolean autoAlign = false;

	/**
	 * The threshold of rpm the shooter needs to be within to shoot in rpm
	 */
	public static final double SHOOT_THRESHOLD = 0;

	/**
	 * The max speed of the shooter, in rotations per minute
	 * TODO: Shooter: Find max speed
	 */
	public static final double MAX_SHOOTER_SPEED = 0;
	
	private Shooter() { 
		if (EnabledSubsystems.SHOOTER_ENABLED) { 
			talon = new CANTalon(RobotMap.SHOOTER_TALON_PORT);
			if(EnabledSubsystems.SHOOTER_DUMB_ENABLED) {
				talon.changeControlMode(TalonControlMode.PercentVbus);
			} else {
				talon.changeControlMode(TalonControlMode.Speed);
			}
			talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
			talon.setF(F);
			talon.setP(P);
			talon.setI(I);
			talon.setD(D);
			talon.enableBrakeMode(false);
			talon.reverseSensor(false); //TODO: Shooter: Find phase
			talon.enable();
			getInstance().setAutoAlign(true);
		}
	}

	public static Shooter getInstance() {
		if(singleton == null)
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
			talon.set(motorSetpoint);
		}
	}
	
	/**
	 * Method that is periodically called once the Shooter class is initialized
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
			if(EnabledSubsystems.SHOOTER_SMARTDASHBOARD_BASIC_ENABLED){
				SmartDashboard.putNumber("Shooter Current", talon.getOutputCurrent());
				SmartDashboard.putString("Shooter Speed", talon.getSpeed() + " : " + getInstance().motorSetpoint);	
			}
			if(EnabledSubsystems.SHOOTER_SMARTDASHBOARD_COMPLEX_ENABLED){
				SmartDashboard.putNumber("Shooter Voltage", talon.getOutputVoltage());
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

	public void setPID(double P, double I, double D, double F) {
		if(talon != null) {
			talon.setPID(P, I, D);
			talon.setF(F);
		}
	}

	/**
	 * Gets the speed of the shooter
	 * 
	 * @return speed of the shooter in rpm
	 */
	public double getSpeed() {
		return talon.getSpeed();
	}
	
	/**
	 * Used to see is the shooter speed is being influenced by camera or by operator
	 * 
	 * @return is the shooter speed in autonomous mode
	 */
	public boolean isAutoAlign() {
		return autoAlign;
	}
	
	/**
	 * Sets the autoAlign mode to true or false 
	 * 
	 * @param autoAlign
	 * 		Is the subsystem going to be auto-aligned
	 */
	public void setAutoAlign(boolean autoAlign) {
		this.autoAlign = autoAlign;
	}

}
