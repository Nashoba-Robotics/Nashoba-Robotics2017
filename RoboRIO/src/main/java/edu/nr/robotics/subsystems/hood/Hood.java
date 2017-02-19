package edu.nr.robotics.subsystems.hood;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.sensorhistory.TalonEncoder;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Hood extends NRSubsystem {
	
	private static Hood singleton;

	private CANTalon talon;
	private TalonEncoder encoder;
	
	/**
	 * The speed in degrees per second that the motor is currently supposed to be running at
	 */
	private double speedSetpoint = 0;
	
	/**
	 * The position in degrees that the hood is currently supposed to be at
	 */
	private double positionSetpoint = 0;
	
	//TODO: Hood: Find FPID values
	public static double F = (Hood.MAX_SPEED / Units.DEGREES_PER_ROTATION / Units.HUNDRED_MS_PER_SECOND * Units.MAGNETIC_NATIVE_UNITS_PER_REV );
	public static double P_MOTION_MAGIC = 0;
	public static double I_MOTION_MAGIC = 0;
	public static double D_MOTION_MAGIC = 0;
	public static double P_OPERATOR_CONTROL = 0;
	public static double I_OPERATOR_CONTROL = 0;
	public static double D_OPERATOR_CONTROL = 0;
	
	/**
	 * Degrees
	 * TODO: Hood: Find top position
	 */
	private static final int TOP_POSITION = 0;
	
	/**
	 * Degrees
	 */
	private static final int BOTTOM_POSITION = 0;

	//CANTalon PID Profile numbers
	private static final int MOTION_MAGIC = 0;
	private static final int OPERATOR_CONTROL = 1;
	
	private boolean autoAlign = false;

	/**
	 * The number of hood degrees around the goal position that we can be at
	 * TODO: Hood: Find the position threshold
	 */
	public static final double POSITION_THRESHOLD = 0;

	/**
	 * The threshold of degrees the hood needs to be within to shoot in rotations
	 * TODO: Hood: Find shoot threshold
	 */
	public static final double SHOOT_THRESHOLD = 0;

	/**
	 * The max acceleration of the hood, in degrees per second per second
	 * TODO: Hood: Find max acceleration
	 */
	public static final double MAX_ACCELERATION = 0;

	/**
	 * The max speed of the hood, in degrees per second
	 * TODO: Hood: Find max speed
	 */
	public static final double MAX_SPEED = 0;
	
	private Hood() { 
		if (EnabledSubsystems.HOOD_ENABLED) { 
			talon = new CANTalon(RobotMap.HOOD_TALON_PORT);
			
			if(EnabledSubsystems.HOOD_DUMB_ENABLED) {
				talon.changeControlMode(TalonControlMode.PercentVbus);
			} else {
				talon.changeControlMode(TalonControlMode.Speed);
			}
			talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
			talon.setPID(P_MOTION_MAGIC, I_MOTION_MAGIC, D_MOTION_MAGIC, F, (int)talon.getIZone(), talon.getCloseLoopRampRate(), MOTION_MAGIC);
			talon.setPID(P_OPERATOR_CONTROL, I_OPERATOR_CONTROL, D_OPERATOR_CONTROL, F, (int)talon.getIZone(), talon.getCloseLoopRampRate(), OPERATOR_CONTROL);
			talon.setProfile(OPERATOR_CONTROL);
			talon.setMotionMagicCruiseVelocity(MAX_SPEED / Units.DEGREES_PER_ROTATION * Units.SECONDS_PER_MINUTE);
			talon.setMotionMagicAcceleration(MAX_ACCELERATION / Units.DEGREES_PER_ROTATION * Units.SECONDS_PER_MINUTE);
			talon.enableBrakeMode(true);
			talon.reverseSensor(false); //TODO: Hood: Find phase
			talon.enable();
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
	
	private double addGearing(double in) {
		return in; //TODO: Hood: Gearing
	}
	
	private double removeGearing(double in) {
		return in; //TODO: Hood: Gearing
	}
	
	/**
	 * Sets motor speed of hood.
	 * 
	 * @param speed
	 *            the hood motor speed, from -1 to 1
	 */
	public void setMotorSpeedInPercent(double speed) {
		setMotorSpeedInDegreesPerSecond(speed * MAX_SPEED);
	}

	/**
	 * Sets motor speed of hood.
	 * 
	 * @param speed
	 *            the hood motor speed, from -{@value #MAX_SPEED} to {@value #MAX_SPEED}
	 */
	public void setMotorSpeedInDegreesPerSecond(double speed) {
		speedSetpoint = speed;
		if (talon != null) {
			CANTalon.TalonControlMode mode = talon.getControlMode();
			if(mode == CANTalon.TalonControlMode.MotionMagic) {
				if(EnabledSubsystems.HOOD_DUMB_ENABLED) {
					talon.changeControlMode(TalonControlMode.PercentVbus);
				} else {
					talon.changeControlMode(TalonControlMode.Speed);
				}
			}
			if(mode == CANTalon.TalonControlMode.PercentVbus) {
				talon.set(addGearing(speedSetpoint / MAX_SPEED));
			} else {
				talon.set(addGearing(speedSetpoint));				
			}
		}
	}

	/**
	 * Gets the current speed of the talon
	 * 
	 * @return current position of talon in degrees per second
	 */
	public double getSpeed() {
		if(talon != null)
			return removeGearing(talon.getSpeed()) * Units.DEGREES_PER_ROTATION / Units.SECONDS_PER_MINUTE;
		return 0;
	}
	
	/**
	 * Set the goal position of the hood. 
	 * 
	 * @param position
	 * 			The goal positions in degrees
	 */
	public void setPosition(double position) {
		positionSetpoint = position;
		if (talon != null) {
			CANTalon.TalonControlMode mode = talon.getControlMode();
			if(mode == CANTalon.TalonControlMode.Speed || mode == CANTalon.TalonControlMode.PercentVbus) {
				talon.changeControlMode(TalonControlMode.MotionMagic);
			}
			talon.set(addGearing(positionSetpoint));
		}

	}

	/**
	 * @return Position in degrees
	 */
	public double getPosition() {
		if(talon != null) {
			return removeGearing(talon.getPosition()) * Units.DEGREES_PER_ROTATION;
		}
		return 0;
	}
	
	/**
	 * Get the position at a time in the past.
	 * @param deltaTime How long ago to look, in milliseconds
	 * @return The position in degrees
	 */
	public double getHistoricalPosition(long deltaTime) {
		if (encoder != null)
			return removeGearing(encoder.getPosition(deltaTime)) * Units.DEGREES_PER_ROTATION;
		return 0;
	}
	
	/**
	 * Function that is periodically called once the Shooter class is initialized
	 */
	@Override
	public void periodic() {
		if(talon != null) {
			//TODO: Hood: Is forward limit switch top or bottom?
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
				SmartDashboard.putString("Hood Speed", getSpeed() + " : " + speedSetpoint);
				SmartDashboard.putString("Hood Position", getPosition() + " : " + positionSetpoint);				
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
		setMotorSpeedInPercent(0);
		setPosition(getPosition());
	}

	public void setPID(double p, double i, double d) {
		if(talon != null) {
			talon.setPID(p, i, d);
		}
	}

	public boolean isMotionMagicMode() {
		if(talon != null) {
			return talon.getControlMode() == TalonControlMode.MotionMagic;
		}
		return false;
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