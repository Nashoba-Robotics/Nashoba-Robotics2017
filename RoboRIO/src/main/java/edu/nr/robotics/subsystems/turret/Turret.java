package edu.nr.robotics.subsystems.turret;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.sensorhistory.TalonEncoder;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularAcceleration;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Angle.Unit;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turret extends NRSubsystem {
	
	public static Turret singleton;

	private CANTalon talon;
	private TalonEncoder encoder;
	
	/**
	 * The speed in degrees per second that the motor is currently supposed to be running at
	 */
	private AngularSpeed speedSetpoint = AngularSpeed.ZERO;
	
	/**
	 * The position that the hood is currently supposed to be at
	 */
	private Angle positionSetpoint = Angle.ZERO;
	
	//TODO: Turret: Find forward versus reverse limit switches
	public static final Angle FORWARD_POSITION = new Angle(-90, Angle.Unit.DEGREE);
	public static final Angle REVERSE_POSITION = new Angle(90, Angle.Unit.DEGREE);
	
	//Profiles
	private static final int MOTION_MAGIC = 0;
	private static final int OPERATOR_CONTROL = 1;
	
	private boolean autoAlign = false;
	
	public int turretTrackDirection = 1;

	/**
	 * The angle around the goal position that we can be at
	 */
	public static final Angle POSITION_THRESHOLD = new Angle(0.5, Angle.Unit.DEGREE);

	/**
	 * The angle threshold the turret needs to be within to shoot
	 */
	public static final Angle SHOOT_THRESHOLD = POSITION_THRESHOLD;

	/**
	 * The angle the turret will automatically turn to start the match
	 */
	public static final Angle PRESET_ANGLE_BLUE = Turret.REVERSE_POSITION;

	/**
	 * The angle the turret will automatically turn to start the match
	 */
	public static final Angle PRESET_ANGLE_RED = Turret.FORWARD_POSITION;

	/**
	 * The percentage of max speed the turret will go when tracking
	 */
	public static final double MAX_TRACKING_PERCENTAGE = 0.1;

	/**
	 * The max acceleration of the turret, in degrees per second per second
	 * TODO: Turret: Find max acceleration
	 */
	public static final AngularAcceleration MAX_ACCELERATION = new AngularAcceleration(1, Angle.Unit.DEGREE, Time.Unit.SECOND, Time.Unit.SECOND);

	/**
	 * The max speed of the turret, in degrees per second
	 * TODO: Turret: Find max speed
	 */
	public static final AngularSpeed MAX_SPEED = new AngularSpeed(1, Angle.Unit.DEGREE, Time.Unit.SECOND);

	//TODO: Turret: Find FPID values
	public static double F = Turret.MAX_SPEED.get(Angle.Unit.MAGNETIC_ENCODER_NATIVE_UNITS, Time.Unit.HUNDRED_MILLISECOND);
	public static double P_MOTION_MAGIC = 0;
	public static double I_MOTION_MAGIC = 0;
	public static double D_MOTION_MAGIC = 0;
	public static double P_OPERATOR_CONTROL = 0;
	public static double I_OPERATOR_CONTROL = 0;
	public static double D_OPERATOR_CONTROL = 0;
	
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
			talon.setMotionMagicCruiseVelocity(MAX_SPEED.get(Angle.Unit.ROTATION, Time.Unit.MINUTE));
			talon.setMotionMagicAcceleration(MAX_ACCELERATION.get(Angle.Unit.ROTATION, Time.Unit.MINUTE, Time.Unit.SECOND));
			talon.enableBrakeMode(true);
			talon.reverseSensor(false); //TODO: Turret: Find phase
			talon.enable();
			setAutoAlign(true);
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
	
	static final double encoderDistancePerRealRotation = 100.0 /*versa planetary*/ * 280.0 /*main gear*/ / 30.0 /*small gear*/;
	
	//static final double forwardEncRotations = 0 / Units.MAGNETIC_NATIVE_UNITS_PER_REV;
	//static final double reverseEncRotations = 0 / Units.MAGNETIC_NATIVE_UNITS_PER_REV;
	//static final double encoderDistancePerRealRotation = forwardEncRotations / FORWARD_POSITION.get(Unit.ROTATION) - reverseEncRotations / REVERSE_POSITION.get(Unit.ROTATION);

	
	private double positionToRaw(Angle in) {
		return in.get(Unit.ROTATION) * encoderDistancePerRealRotation;
	}
	
	private double speedToRaw(AngularSpeed in) {
		return in.get(Angle.Unit.ROTATION, Time.Unit.MINUTE) * encoderDistancePerRealRotation;
	}
	
	private Angle rawToPosition(double in) {
		return new Angle(in / encoderDistancePerRealRotation, Angle.Unit.ROTATION);
	}
	
	private AngularSpeed rawToSpeed(double in) {
		return new AngularSpeed(in / encoderDistancePerRealRotation, Angle.Unit.ROTATION, Time.Unit.MINUTE);
	}
	
	/**
	 * Sets motor speed of hood.
	 * 
	 * @param speed
	 *            the hood motor speed, from -1 to 1
	 */
	public void setMotorSpeedInPercent(double speed) {
		setMotorSpeedInDegreesPerSecond(MAX_SPEED.mul(speed));
	}

	/**
	 * Sets motor speed of hood.
	 * 
	 * @param speed
	 *            the hood motor speed, from -MAX_SPEED to MAX_SPEED
	 */
	public void setMotorSpeedInDegreesPerSecond(AngularSpeed speed) {
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
				talon.set(speedToRaw(speedSetpoint) / speedToRaw(MAX_SPEED));
			} else {
				talon.set(speedToRaw(speedSetpoint));				
			}
		}
	}
	
	/**
	 * Gets the current speed of the talon
	 * 
	 * @return current position of talon in degrees per second
	 */
	public AngularSpeed getSpeed() {
		if(talon != null)
			return rawToSpeed(talon.getSpeed());
		return AngularSpeed.ZERO;
	}
	
	/**
	 * Set the goal position of the turret. 
	 * 
	 * @param position
	 * 			The goal position
	 */
	public void setPosition(Angle position) {
		positionSetpoint = position;
		if (talon != null) {
			CANTalon.TalonControlMode mode = talon.getControlMode();
			if(mode == CANTalon.TalonControlMode.Speed || mode == CANTalon.TalonControlMode.PercentVbus) {
				talon.changeControlMode(TalonControlMode.MotionMagic);
			}
			talon.set(positionToRaw(positionSetpoint));
		}

	}
	
	/**
	 * Gets the current position of the talon
	 * 
	 * @return current position of talon in degrees
	 */
	public Angle getPosition() {
		if(talon != null)
			return rawToPosition(talon.getPosition());
		return Angle.ZERO;
	}
	
	/**
	 * Get the historical value of the talon
	 * @param deltaTime how long ago to look, in milliseconds
	 * @return in degrees
	 */
	public Angle getHistoricalPosition(Time deltaTime) {
		if (encoder != null)
			return rawToPosition(encoder.getPosition(deltaTime));
		return Angle.ZERO;
	}
	
	/**
	 * Function that is periodically called once the Shooter class is initialized
	 */
	@Override
	public void periodic() {
		if(talon != null) {
			if(talon.isFwdLimitSwitchClosed()) { //TODO: Turret: Check limit switch direction
				talon.setPosition(FORWARD_POSITION.get(Unit.ROTATION));
				turretTrackDirection = -1;
			} else if(talon.isRevLimitSwitchClosed()) {
				talon.setPosition(REVERSE_POSITION.get(Unit.ROTATION));
				turretTrackDirection = 1;
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
				SmartDashboard.putString("Turret Speed", getSpeed() + " : " + speedSetpoint);
				SmartDashboard.putString("Turret Position", getPosition() + " : " + positionSetpoint);	
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
		setMotorSpeedInPercent(0);
		setPosition(getPosition());
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
