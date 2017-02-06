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
	
	public static Hood singleton;

	private CANTalon talon;
	
	public double speedSetpoint = 0;
	public double positionSetpoint = 0;
	
	private static final int TICKS_PER_REV = 256;

	//TODO: Hood: Find FPID values
	public static double F = (RobotMap.MAX_HOOD_SPEED / RobotMap.HUNDRED_MS_PER_MIN * RobotMap.NATIVE_UNITS_PER_REV);
	public static double P_MOTION_MAGIC = 0;
	public static double I_MOTION_MAGIC = 0;
	public static double D_MOTION_MAGIC = 0;
	public static double P_OPERATOR_CONTROL = 0;
	public static double I_OPERATOR_CONTROL = 0;
	public static double D_OPERATOR_CONTROL = 0;
	
	public static final int TOP_POSITION = 0; //TODO: Hood: Find top position
	public static final int BOTTOM_POSITION = 0; //TODO: Hood: Find bottom position

	public static final int MOTION_MAGIC = 0;
	public static final int OPERATOR_CONTROL = 1;
	
	private Hood() { 
		if (EnabledSubsystems.HOOD_ENABLED) { 
			talon = new CANTalon(RobotMap.HOOD_TALON);
			
			talon.changeControlMode(TalonControlMode.PercentVbus);
			talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			talon.setPID(P_MOTION_MAGIC, I_MOTION_MAGIC, D_MOTION_MAGIC, F, (int)talon.getIZone(), talon.getCloseLoopRampRate(), MOTION_MAGIC);
			talon.setPID(P_OPERATOR_CONTROL, I_OPERATOR_CONTROL, D_OPERATOR_CONTROL, F, (int)talon.getIZone(), talon.getCloseLoopRampRate(), OPERATOR_CONTROL);
			talon.setProfile(OPERATOR_CONTROL);
			talon.setMotionMagicCruiseVelocity(RobotMap.MAX_HOOD_SPEED);
			talon.setMotionMagicAcceleration(RobotMap.MAX_HOOD_ACCELERATION);
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
		speedSetpoint = speed * RobotMap.HOOD_DIRECTION;
		if (talon != null) {
			CANTalon.TalonControlMode mode = talon.getControlMode();
			if(mode == CANTalon.TalonControlMode.PercentVbus || mode == CANTalon.TalonControlMode.Speed) {
				talon.set(speedSetpoint);
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
		positionSetpoint = position * RobotMap.HOOD_DIRECTION;
		if (talon != null) {
			CANTalon.TalonControlMode mode = talon.getControlMode();
			if(mode == CANTalon.TalonControlMode.MotionMagic) {
				talon.set(positionSetpoint);
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
		setPosition(talon.getPosition());
	}

	public void setPID(double p, double i, double d) {
		if(talon != null) {
			talon.setPID(p, i, d);
		}
	}

	public boolean isMotionMagicMode() {
		return (talon.getControlMode() == TalonControlMode.MotionMagic);
	}
	
}
