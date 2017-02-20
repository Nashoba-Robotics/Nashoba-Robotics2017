package edu.nr.robotics.subsystems.turret;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.sensorhistory.TalonEncoder;
import edu.nr.lib.units.Angle;
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
	
	public static final Angle FORWARD_POSITION = Angle.ZERO; //TODO: Turret: Find forward position
	public static final Angle REVERSE_POSITION = Angle.ZERO; //TODO: Turret: Find reverse position
	
	//Profiles
	private static final int MOTION_MAGIC = 0;
	private static final int OPERATOR_CONTROL = 1;
	
	private boolean autoAlign = false;
	
	public int turretTrackDirection = 1;

	/**
	 * The angle around the goal position that we can be at
	 * TODO: Turret: Find the position threshold
	 */
	public static final Angle POSITION_THRESHOLD = Angle.ZERO;

	/**
	 * The angle threshold the turret needs to be within to shoot
	 * TODO: Turret: Get shoot threshold
	 */
	public static final Angle SHOOT_THRESHOLD = Angle.ZERO;

	/**
	 * The angle the turret will automatically turn to start the match
	 * 
	 * TODO: Turret: Get preset turret angle for blue side
	 */
	public static final Angle PRESET_ANGLE_BLUE = Angle.ZERO;

	/**
	 * The angle the turret will automatically turn to start the match
	 * 
	 * TODO: Turret: Get preset turret angle for red side
	 */
	public static final Angle PRESET_ANGLE_RED = Angle.ZERO;

	/**
	 * The percentage of max speed the turret will go when tracking
	 * 
	 * TODO: Turret: Determine the percentage of max speed the turret will go when tracking
	 */
	public static final double MAX_TRACKING_PERCENTAGE = 0;

	/**
	 * The max acceleration of the turret, in degrees per second per second
	 * TODO: Turret: Find max acceleration
	 */
	public static final double MAX_ACCELERATION = 0;

	/**
	 * The max speed of the turret, in degrees per second
	 * TODO: Turret: Find max speed
	 */
	public static final AngularSpeed MAX_SPEED = AngularSpeed.ZERO;

	//TODO: Turret: Find FPID values
	public static double F = Turret.MAX_SPEED.get(AngularSpeed.Unit.RPS) / Units.HUNDRED_MS_PER_SECOND * Units.MAGNETIC_NATIVE_UNITS_PER_REV;
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
			talon.setMotionMagicCruiseVelocity(MAX_SPEED.get(AngularSpeed.Unit.RPM));
			talon.setMotionMagicAcceleration(MAX_ACCELERATION / Units.DEGREES_PER_ROTATION * Units.SECONDS_PER_MINUTE);
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
	
	private double addGearing(double in) {
		return in; //TODO: Turret: Gearing
	}
	
	private double removeGearing(double in) {
		return in; //TODO: Turret: Gearing
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
				talon.set(addGearing(speedSetpoint.div(MAX_SPEED)));
			} else {
				talon.set(addGearing(speedSetpoint.get(AngularSpeed.Unit.RPM)));				
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
			return new AngularSpeed(removeGearing(talon.getSpeed()), AngularSpeed.Unit.RPM);
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
			talon.set(addGearing(positionSetpoint.get(Unit.ROTATION)));
		}

	}
	
	/**
	 * Gets the current position of the talon
	 * 
	 * @return current position of talon in degrees
	 */
	public Angle getPosition() {
		if(talon != null)
			return new Angle(removeGearing(talon.getPosition()), Angle.Unit.ROTATION);
		return Angle.ZERO;
	}
	
	/**
	 * Get the historical value of the talon
	 * @param deltaTime how long ago to look, in milliseconds
	 * @return in degrees
	 */
	public Angle getHistoricalPosition(Time deltaTime) {
		if (encoder != null)
			return new Angle(removeGearing(encoder.getPosition(deltaTime)), Angle.Unit.ROTATION);
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
