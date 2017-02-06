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

	//TODO: Turret: Set up MagicMotion mode
	
	public static Turret singleton;

	private CANTalon talon;
	
	public double speedSetpoint = 0;
	public double positionSetpoint = 0;

	//TODO: Turret: Find FPID values
	public static double F = (RobotMap.MAX_TURRET_SPEED / RobotMap.HUNDRED_MS_PER_MIN * RobotMap.NATIVE_UNITS_PER_REV);
	public static double P_MOTION_MAGIC = 0;
	public static double I_MOTION_MAGIC = 0;
	public static double D_MOTION_MAGIC = 0;
	public static double P_OPERATOR_CONTROL = 0;
	public static double I_OPERATOR_CONTROL = 0;
	public static double D_OPERATOR_CONTROL = 0;
	
	public static final int FORWARD_POSITION = 0; //TODO: Turret: Find forward position
	public static final int REVERSE_POSITION = 0; //TODO: Turret: Find reverse position
	
	public static final int MOTION_MAGIC = 0;
	public static final int OPERATOR_CONTROL = 1;
	
	private Turret() { 
		if (EnabledSubsystems.TURRET_ENABLED) { 
			talon = new CANTalon(RobotMap.TURRET_TALON);
			
			talon.changeControlMode(TalonControlMode.PercentVbus);
			talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			talon.setPID(P_MOTION_MAGIC, I_MOTION_MAGIC, D_MOTION_MAGIC, F, (int)talon.getIZone(), talon.getCloseLoopRampRate(), MOTION_MAGIC);
			talon.setPID(P_OPERATOR_CONTROL, I_OPERATOR_CONTROL, D_OPERATOR_CONTROL, F, (int)talon.getIZone(), talon.getCloseLoopRampRate(), OPERATOR_CONTROL);
			talon.setMotionMagicCruiseVelocity(RobotMap.MAX_TURRET_SPEED);
			talon.setMotionMagicAcceleration(RobotMap.MAX_TURRET_ACCELERATION);
			talon.configEncoderCodesPerRev(RobotMap.TICKS_PER_REV);
			talon.enableBrakeMode(true);
			talon.setEncPosition(0);
			talon.reverseSensor(false); //TODO: Turret: Find phase
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
	 * Sets motor speed of turret.
	 * 
	 * If not in speed or percentVbus mode, this does nothing.
	 * 
	 * @param speed
	 *            the turret motor speed, 
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
			if(mode == CANTalon.TalonControlMode.MotionMagic) {
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
			SmartDashboard.putString("Turret Speed", talon.getSpeed() + " : " + getInstance().speedSetpoint);
			SmartDashboard.putString("Turret Position", talon.getPosition() + " : " + getInstance().positionSetpoint);
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
