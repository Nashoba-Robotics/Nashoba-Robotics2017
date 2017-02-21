package edu.nr.robotics.subsystems.shooter;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.commandbased.DoNothingJoystickCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends NRSubsystem {

	private static Shooter singleton;

	private CANTalon talon;

	/**
	 * The max speed of the shooter
	 * TODO: Shooter: Find max speed
	 */
	public static final AngularSpeed MAX_SPEED = new AngularSpeed(1, Angle.Unit.DEGREE, Time.Unit.SECOND);
	
	/**
	 * The speed that the motor is currently supposed to be running at.
	 * 
	 * The initial value is the speed it is supposed to run at to start the match.
	 */
	public AngularSpeed motorSetpoint = MAX_SPEED.mul(0.3);

	private boolean autoAlign = false;

	/**
	 * The threshold the shooter needs to be within to shoot
	 */
	public static final AngularSpeed SHOOT_THRESHOLD = new AngularSpeed(50, Angle.Unit.ROTATION, Time.Unit.MINUTE);

	//TODO: Shooter: Find FPID values
	public static double F = Shooter.MAX_SPEED.get(Angle.Unit.MAGNETIC_ENCODER_NATIVE_UNITS, Time.Unit.HUNDRED_MILLISECOND);
	public static double P = 0;
	public static double I = 0;
	public static double D = 0;
	
	private Shooter() { 
		if (EnabledSubsystems.SHOOTER_ENABLED) {
			talon = new CANTalon(RobotMap.SHOOTER_A_TALON_PORT);
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
			setAutoAlign(true);
			
			CANTalon tempTalon = new CANTalon(RobotMap.SHOOTER_B_TALON_PORT);
			tempTalon.changeControlMode(TalonControlMode.Follower);
			tempTalon.set(talon.getDeviceID());
			tempTalon.enableBrakeMode(false);
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
			singleton.setJoystickCommand(new ShooterJoystickCommand());
		}
	}

	/**
	 * Sets motor speed of shooter
	 * 
	 * @param speed
	 *            the shooter motor speed,  from -1 to 1
	 */
	public void setMotorSpeedPercent(double speed) {
		setMotorSpeedInRPM(MAX_SPEED.mul(speed));
	}

	/**
	 * Sets motor speed of shooter
	 * 
	 * @param speed
	 *            the shooter motor speed, from -MAX_SPEED to MAX_SPEED
	 */
	public void setMotorSpeedInRPM(AngularSpeed speed) {
		motorSetpoint = speed;
		if (talon != null && OI.getInstance().isShooterOn()) {
			if(talon.getControlMode() == TalonControlMode.Speed) {
				talon.set(motorSetpoint.get(Angle.Unit.ROTATION, Time.Unit.MINUTE));
			} else {
				talon.set(motorSetpoint.div(MAX_SPEED));
			}
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
				SmartDashboard.putString("Shooter Speed", getSpeed().get(Angle.Unit.ROTATION, Time.Unit.MINUTE) + " : " + motorSetpoint.get(Angle.Unit.ROTATION, Time.Unit.MINUTE));	
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
		setMotorSpeedPercent(0);
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
	 * @return speed of the shooter
	 */
	public AngularSpeed getSpeed() {
		if(talon != null) {
			return new AngularSpeed(talon.getSpeed(), Angle.Unit.ROTATION, Time.Unit.MINUTE);
		}
		return AngularSpeed.ZERO;
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
