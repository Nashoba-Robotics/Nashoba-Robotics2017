package edu.nr.robotics.subsystems.turret;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.sensorhistory.sf2.HistoricalCANTalon;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turret extends NRSubsystem {
	
	public static Turret singleton;

	private HistoricalCANTalon talon;
	
	public double speedSetpoint = 0;
	public double positionSetpoint = 0;

	private static final int TICKS_PER_REV = 256; //TODO: Turret: Get ticks per revolution
	public static final int TURRET_TICKS_PER_REV = 256; //TODO: Turret: Get ticks per revolution of actual turret
	private static final int NATIVE_UNITS_PER_REV = 4*TICKS_PER_REV;

	//TODO: Turret: Find FPID values
	public static double F = (RobotMap.MAX_TURRET_SPEED / RobotMap.HUNDRED_MS_PER_MIN * NATIVE_UNITS_PER_REV);
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
	
	private Turret() { 
		if (EnabledSubsystems.TURRET_ENABLED) { 
			talon = new HistoricalCANTalon(RobotMap.TURRET_TALON_PORT);
			
			talon.changeControlMode(TalonControlMode.PercentVbus);
			talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			talon.setPID(P_MOTION_MAGIC, I_MOTION_MAGIC, D_MOTION_MAGIC, F, (int)talon.getIZone(), talon.getCloseLoopRampRate(), MOTION_MAGIC);
			talon.setPID(P_OPERATOR_CONTROL, I_OPERATOR_CONTROL, D_OPERATOR_CONTROL, F, (int)talon.getIZone(), talon.getCloseLoopRampRate(), OPERATOR_CONTROL);
			talon.setMotionMagicCruiseVelocity(RobotMap.MAX_TURRET_SPEED);
			talon.setMotionMagicAcceleration(RobotMap.MAX_TURRET_ACCELERATION);
			talon.configEncoderCodesPerRev(TICKS_PER_REV);
			talon.enableBrakeMode(true);
			talon.setEncPosition(0);
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
		speedSetpoint = speed * RobotMap.TURRET_DIRECTION;
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
