package edu.nr.robotics.subsystems.hood;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.sensorhistory.sf2.HistoricalCANTalon;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Hood extends NRSubsystem {
	
	public static Hood singleton;

	private HistoricalCANTalon talon;
	
	public double speedSetpoint = 0;
	public double positionSetpoint = 0;
	
	private static final int TICKS_PER_REV = 256; //TODO: Hood: Get ticks per revolution
	private static final int NATIVE_UNITS_PER_REV = 4*TICKS_PER_REV;

	//TODO: Hood: Find FPID values
	public static double F = (RobotMap.MAX_HOOD_SPEED / RobotMap.HUNDRED_MS_PER_MIN * NATIVE_UNITS_PER_REV);
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
	
	private boolean autoAlign = false;
	
	private Hood() { 
		if (EnabledSubsystems.HOOD_ENABLED) { 
			talon = new HistoricalCANTalon(RobotMap.HOOD_TALON_PORT);
			
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
			getInstance().setAutoAlign(true);
		}
	}

	public static Hood getInstance() {
		if (singleton == null)
			init();
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new Hood();
			singleton.setJoystickCommand(new HoodJoystickCommand());
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
			if(mode == CANTalon.TalonControlMode.MotionMagic) {
				talon.changeControlMode(TalonControlMode.Speed);
			}
			talon.set(speedSetpoint);
		}
	}
	
	/**
	 * Set the goal position of the hood. 
	 * 
	 * If the hood is not in position mode, this does nothing.
	 * 
	 * @param position
	 * 			The goal positions in rotations
	 */
	public void setPosition(double position) {
		positionSetpoint = position * TICKS_PER_REV * RobotMap.HOOD_DIRECTION;
		if (talon != null) {
			CANTalon.TalonControlMode mode = talon.getControlMode();
			if(mode == CANTalon.TalonControlMode.Speed) {
				talon.changeControlMode(TalonControlMode.MotionMagic);
				talon.set(positionSetpoint);
			} else if(mode == CANTalon.TalonControlMode.MotionMagic) {
				talon.set(positionSetpoint);
			}
		}

	}
	
	public double getHistoricalPosition(double deltaTime) {
		if (talon != null)
			return talon.getHistoricalPosition(deltaTime);
		return 0;
	}
	
	/**
	 * This sets the change in position of the turret in encoder ticks
	 * 
	 * @param deltaPosition
	 */
	public void setPositionDelta(double deltaPosition) {
		positionSetpoint = getInstance().getPosition() + deltaPosition * TICKS_PER_REV * RobotMap.HOOD_DIRECTION;
		if (talon != null) {
			CANTalon.TalonControlMode mode = talon.getControlMode();
			if(mode == CANTalon.TalonControlMode.Speed) {
				talon.changeControlMode(TalonControlMode.MotionMagic);
				getInstance().setPosition(getInstance().getPosition() + deltaPosition);
			} else if(mode == CANTalon.TalonControlMode.MotionMagic) {
				getInstance().setPosition(getInstance().getPosition() + deltaPosition);
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
			if(EnabledSubsystems.HOOD_SMARTDASHBOARD_BASIC_ENABLED) {
				SmartDashboard.putNumber("Hood Current", talon.getOutputCurrent());
				SmartDashboard.putString("Hood Speed", talon.getSpeed() + " : " + getInstance().speedSetpoint);
				SmartDashboard.putString("Hood Position", talon.getPosition() + " : " + getInstance().positionSetpoint);				
			}
			if(EnabledSubsystems.HOOD_SMARTDASHBOARD_COMPLEX_ENABLED) {
				SmartDashboard.putNumber("Hood Voltage", talon.getOutputVoltage());
			}
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

	public double getPosition() {
		return talon.getPosition();
	}
	
	/**
	 * Used to see is the hood angle is being influenced by camera or by operator
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
