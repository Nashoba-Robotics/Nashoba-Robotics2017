package edu.nr.robotics.subsystems.hood;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.DoNothingJoystickCommand;
import edu.nr.lib.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Hood extends NRSubsystem {

	//TODO: Hood: Set up MagicMotion mode
	
	public static Hood singleton;

	private CANTalon talon;
	
	public double speedSetpoint = 0;
	public double positionSetpoint = 0;
	
	private static final int TICKS_PER_REV = 256;

	//TODO: Hood: Find FPID values
	public static double F = 0;
	public static double P = 0;
	public static double I = 0;
	public static double D = 0;
	
	public static final int TOP_POSITION = 0; //TODO: Hood: Find top position
	public static final int BOTTOM_POSITION = 0; //TODO: Hood: Find bottom position

	
	private Hood() { 
		if (EnabledSubsystems.HOOD_ENABLED) { 
			talon = new CANTalon(RobotMap.HOOD_TALON);
			
			talon.changeControlMode(TalonControlMode.PercentVbus);
			talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			talon.setF(F);
			talon.setP(P);
			talon.setI(I);
			talon.setD(D);
			talon.configEncoderCodesPerRev(TICKS_PER_REV);
			talon.enableBrakeMode(true);
			talon.setEncPosition(0);
			talon.reverseSensor(false); //TODO: Hood: Find phase
			talon.enable();
		}
	}

	public static Hood getInstance() {
		init();
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new Hood();
			singleton.setJoystickCommand(new DoNothingJoystickCommand(singleton));
		}
	}

	/**
	 * Sets motor speed of hood.
	 * 
	 * If not in speed or percentVbus mode, this does nothing.
	 * 
	 * @param speed
	 *            the hood motor speed, 
	 *            
	 *            If the talon mode is Speed, from -MAX_RPM to MAX_RPM
	 *            If the talon mode is PercentVbus, from -1 to 1
	 */
	public void setMotorSpeed(double speed) {
		speedSetpoint = speed;
		if (talon != null) {
			CANTalon.TalonControlMode mode = talon.getControlMode();
			if(mode == CANTalon.TalonControlMode.PercentVbus || mode == CANTalon.TalonControlMode.Speed) {
				talon.set(speed);
			}
		}
	}
	
	/**
	 * Set the goal position of the robot. 
	 * 
	 * If the robot is not in position mode, this does nothing.
	 * 
	 * @param position
	 * 			The goal positions in rotations
	 */
	public void setPosition(double position) {
		positionSetpoint = position;
		if (talon != null) {
			CANTalon.TalonControlMode mode = talon.getControlMode();
			if(mode == CANTalon.TalonControlMode.PercentVbus || mode == CANTalon.TalonControlMode.Speed) {
				talon.set(position);
			}
		}

	}
	
	/**
	 * Function that is periodically called once the Shooter class is initialized
	 */
	@Override
	public void periodic() {
		if(talon != null) {
			if(talon.isFwdLimitSwitchClosed()) {
				talon.setEncPosition(TOP_POSITION);
			} else if(talon.isRevLimitSwitchClosed()) {
				talon.setEncPosition(BOTTOM_POSITION);
			} 
		}

	}

	/**
	 * Sends data to SmartDashboard
	 */
	@Override
	public void smartDashboardInfo() {
		if (talon != null) {
			SmartDashboard.putNumber("Hood Current", talon.getOutputCurrent());
			SmartDashboard.putNumber("Hood Voltage", talon.getOutputVoltage());
			SmartDashboard.putString("Hood Speed", talon.getSpeed() + " : " + getInstance().speedSetpoint);
			SmartDashboard.putString("Hood Position", talon.getPosition() + " : " + getInstance().positionSetpoint);
		}
	}

	/**
	 * What subsystem does upon robot being disabled
	 */
	@Override
	public void disable() {
		setMotorSpeed(0);
	}

	public void setPID(double p, double i, double d) {
		if(talon != null) {
			talon.setPID(p, i, d);
		}
		
	}

}
