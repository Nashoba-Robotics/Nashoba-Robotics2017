package edu.nr.robotics.subsystems.turret;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.sensorhistory.TalonEncoder;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turret extends NRSubsystem {
	
	public static Turret singleton;

	private CANTalon talon;
	private TalonEncoder encoder;
	
	public double speedSetpoint = 0;
	public double positionSetpoint = 0;

	public static final int TURRET_TICKS_PER_REV = 256; //TODO: Turret: Get ticks per revolution of actual turret

	//TODO: Turret: Find FPID values
	public static double F = (Turret.MAX_TURRET_SPEED / Units.HUNDRED_MS_PER_MIN * Units.MAGNETIC_NATIVE_UNITS_PER_REV);
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
	
	private boolean autoAlign = false;
	
	public int turretTrackDirection = 1;

	/**
	 * The number of turret rotations around the goal position that we can be at
	 * TODO: Turret: Find the position threshold
	 */
	public static final double POSITION_THRESHOLD = 0;

	/**
	 * The threshold of degrees the turret needs to be within to shoot in degrees
	 */
	public static final double SHOOT_THRESHOLD = 0;

	public static final double PRESET_ANGLE_BLUE = 0;

	/**
	 * The angle the turret will automatically turn to start the match in degrees
	 * 
	 * TODO: Get preset turret angles for red and blue sides
	 */
	public static final double PRESET_ANGLE_RED = 0;

	/**
	 * The percentage of max speed the turret will go when tracking
	 * 
	 * TODO: Turret: Determine the percentage of max speed the turret will go when tracking
	 */
	public static final double MAX_TRACKING_PERCENTAGE = 0;

	/**
	 * The max acceleration of the turret, in rotations per minute per second
	 * TODO: Turret: Find max acceleration
	 */
	public static final double MAX_TURRET_ACCELERATION = 0;

	/**
	 * The max speed of the turret, in rotations per minute
	 * TODO: Turret: Find max speed
	 */
	public static final double MAX_TURRET_SPEED = 0;
	
	private Turret() { 
		if (EnabledSubsystems.TURRET_ENABLED) { 
			talon = new CANTalon(RobotMap.TURRET_TALON_PORT);
			
			if(EnabledSubsystems.TURRET_DUMB_ENABLED) {
				talon.changeControlMode(TalonControlMode.PercentVbus);
			} else {
				talon.changeControlMode(TalonControlMode.Speed);
			}
			talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
			talon.setPID(P_MOTION_MAGIC, I_MOTION_MAGIC, D_MOTION_MAGIC, F, (int)talon.getIZone(), talon.getCloseLoopRampRate(), MOTION_MAGIC);
			talon.setPID(P_OPERATOR_CONTROL, I_OPERATOR_CONTROL, D_OPERATOR_CONTROL, F, (int)talon.getIZone(), talon.getCloseLoopRampRate(), OPERATOR_CONTROL);
			talon.setMotionMagicCruiseVelocity(Turret.MAX_TURRET_SPEED);
			talon.setMotionMagicAcceleration(Turret.MAX_TURRET_ACCELERATION);
			talon.enableBrakeMode(true);
			talon.reverseSensor(false); //TODO: Turret: Find phase
			talon.enable();
			getInstance().setAutoAlign(true);
		}
	}

	public static Turret getInstance() {
		if (singleton == null)
			init();
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new Turret();
			singleton.setJoystickCommand(new TurretJoystickCommand());
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
			if(mode == CANTalon.TalonControlMode.MotionMagic) {
				talon.changeControlMode(TalonControlMode.Speed);
			}
			talon.set(speedSetpoint);
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
			if(mode == CANTalon.TalonControlMode.Speed) {
				talon.changeControlMode(TalonControlMode.MotionMagic);
				talon.set(positionSetpoint);
			} else if(mode == CANTalon.TalonControlMode.MotionMagic) {
				talon.set(positionSetpoint);
			}
		}
	}
	
	/**
	 * Gets the current position of the talon
	 * 
	 * @return current position of talon
	 */
	public double getPosition() {
		if(talon != null)
			return talon.getPosition();
		return 0;
	}
	
	public double getHistoricalPosition(long deltaTime) {
		if (encoder != null)
			return encoder.getPosition(deltaTime);
		return 0;
	}
	
	/**
	 * This sets the change in position of the turret in revolutions
	 * 
	 * @param deltaPosition
	 */
	public void setPositionDelta(double deltaPosition) {
		positionSetpoint = getInstance().getPosition() + deltaPosition;
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
				talon.setEncPosition(FORWARD_POSITION);
				getInstance().turretTrackDirection = -1;
			} else if(talon.isRevLimitSwitchClosed()) {
				talon.setEncPosition(REVERSE_POSITION);
				getInstance().turretTrackDirection = 1;
			} 
		}

	}

	/**
	 * Sends data to SmartDashboard
	 */
	@Override
	public void smartDashboardInfo() {
		if (talon != null) {
			if(EnabledSubsystems.TURRET_SMARTDASHBOARD_BASIC_ENABLED){
				SmartDashboard.putNumber("Turret Current", talon.getOutputCurrent());
				SmartDashboard.putString("Turret Speed", talon.getSpeed() + " : " + getInstance().speedSetpoint);
				SmartDashboard.putString("Turret Position", talon.getPosition() + " : " + getInstance().positionSetpoint);	
			}
			if(EnabledSubsystems.TURRET_SMARTDASHBOARD_COMPLEX_ENABLED){
				SmartDashboard.putNumber("Turret Voltage", talon.getOutputVoltage());
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

	/**
	 * Checks to see if the talon is in motion magic mode
	 * 
	 * @return is the talon in motion magic mode
	 */
	public boolean isMotionMagicMode() {
		if(talon != null)
			return (talon.getControlMode() == TalonControlMode.MotionMagic);
		
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
