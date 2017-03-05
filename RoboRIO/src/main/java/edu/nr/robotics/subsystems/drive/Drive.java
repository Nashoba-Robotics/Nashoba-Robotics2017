package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRMath;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.interfaces.DoublePIDOutput;
import edu.nr.lib.interfaces.DoublePIDSource;
import edu.nr.lib.sensorhistory.TalonEncoder;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends NRSubsystem implements DoublePIDOutput, DoublePIDSource {

	private static Drive singleton;

	private CANTalon leftTalon, rightTalon, tempLeftTalon, tempRightTalon;
	private TalonEncoder leftEncoder, rightEncoder;

	private DoubleSolenoid gearSwitcher;

	/**
	 * The diameter of the wheels in inches.
	 * 
	 * This should not be used. Instead {@link WHEEL_DIAMETER} should be used.
	 */
	public static final double WHEEL_DIAMETER_INCHES = 4;

	/**
	 * The distance the wheel travels in a single revolution, in inches
	 * 
	 * This is equivalent to the circumference of the wheel
	 * 
	 * This should not be used. Instead {@link DISTANCE_PER_REV} should be used.
	 */
	public static final double DISTANCE_PER_REV_INCHES = 4 * Math.PI;

	/**
	 * The diameter of the wheels
	 */
	public static final Distance WHEEL_DIAMETER = new Distance(WHEEL_DIAMETER_INCHES, Distance.Unit.INCH);

	/**
	 * The distance the wheel travels in a single revolution
	 * 
	 * This is equivalent to the circumference of the wheel
	 */
	static final Distance DISTANCE_PER_REV = WHEEL_DIAMETER.mul(Math.PI);

	/**
	 * The max driving speed of the robot in low gear 
	 * TODO: Drive: Get max low gear speed
	 */
	public static final Speed MAX_LOW_GEAR_SPEED = new Speed(1, Distance.Unit.METER, Time.Unit.SECOND);

	/**
	 * The max driving speed of the robot in high gear 
	 * TODO: Drive: Get max high gear speed
	 */
	public static final Speed MAX_HIGH_GEAR_SPEED = new Speed(1, Distance.Unit.METER, Time.Unit.SECOND);

	/**
	 * The max driving acceleration in feet/sec/sec
	 * 
	 * TODO: Drive: Get max acceleration
	 */
	public static final double MAX_ACCELERATION = 0;

	/**
	 * The max drive jerk in feet/sec/sec/sec
	 * 
	 * TODO: Drive: Get max jerk
	 */
	public static final double MAX_JERK = 0;

	public static final Distance WHEEL_BASE = new Distance(26.1, Distance.Unit.INCH);

	/**
	 * The number of encoder ticks per wheel revolution
	 */
	public static final int TICKS_PER_REV = 2048; 

	/**
	 * The number of CANTalon "Native Units" per revolution
	 */
	private static final int NATIVE_UNITS_PER_REV = 4 * TICKS_PER_REV;

	/**
	 * The speed in RPM that the left motors are currently supposed to be
	 * running at
	 */
	private Speed leftMotorSetpoint = Speed.ZERO;

	/**
	 * The speed in RPM that the right motors are currently supposed to be
	 * running at
	 */
	private Speed rightMotorSetpoint = Speed.ZERO;

	// TODO: Drive: Find low gear FPID values
	public static final double F_LOW_GEAR = MAX_LOW_GEAR_SPEED.get(Distance.Unit.DRIVE_ROTATION,
			Time.Unit.HUNDRED_MILLISECOND) * NATIVE_UNITS_PER_REV;
	public static final double P_LOW_GEAR = 0;
	public static final double I_LOW_GEAR = 0;
	public static final double D_LOW_GEAR = 0;

	// TODO: Drive: Find high gear FPID values
	public static final double F_HIGH_GEAR = MAX_HIGH_GEAR_SPEED.get(Distance.Unit.DRIVE_ROTATION,
			Time.Unit.HUNDRED_MILLISECOND) * NATIVE_UNITS_PER_REV;
	public static final double P_HIGH_GEAR = 0;
	public static final double I_HIGH_GEAR = 0;
	public static final double D_HIGH_GEAR = 0;

	public static enum DriveMode {
		arcadeDrive, tankDrive
	}

	public static enum Gear {
		high, low;

		// TODO: Drive: Find which gear directions are forward/reverse
		private static Value HIGH_VALUE = Value.kForward;
		private static Value LOW_VALUE = Value.kReverse;

		private static int HIGH_PROFILE = 0;
		private static int LOW_PROFILE = 1;
	}

	private int getCurrentGearProfile() {
		if (getCurrentGear() == Gear.high) {
			return Gear.HIGH_PROFILE;
		} else {
			return Gear.LOW_PROFILE;
		}
	}

	private Speed currentMaxSpeed() {
		if (getCurrentGear() == Gear.low) {
			return MAX_LOW_GEAR_SPEED;
		} else {
			return MAX_HIGH_GEAR_SPEED;
		}
	}

	/**
	 * Position difference compared to end profiler
	 */
	public static final Distance PROFILE_POSITION_THRESHOLD = new Distance(1, Distance.Unit.INCH);

	/**
	 * Delta time checked for to compare talon positions to previous positions
	 * to end profiler
	 */
	public static final Time PROFILE_TIME_THRESHOLD = new Time(200, Time.Unit.MILLISECOND);

	private Drive() {

		if (EnabledSubsystems.DRIVE_ENABLED) {

			gearSwitcher = new DoubleSolenoid(RobotMap.DRIVE_GEAR_SWITCHER_PCM_PORT,
					RobotMap.DRIVE_GEAR_SWITCHER_FORWARD_CHANNEL, RobotMap.DRIVE_GEAR_SWITCHER_REVERSE_CHANNEL);

			leftTalon = new CANTalon(RobotMap.DRIVE_LEFT_F_TALON_PORT);

			if (EnabledSubsystems.DRIVE_DUMB_ENABLED) {
				leftTalon.changeControlMode(TalonControlMode.PercentVbus);
			} else {
				leftTalon.changeControlMode(TalonControlMode.Speed);
			}
			leftTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			leftTalon.setProfile(Gear.LOW_PROFILE);
			leftTalon.setF(F_LOW_GEAR);
			leftTalon.setP(P_LOW_GEAR);
			leftTalon.setI(I_LOW_GEAR);
			leftTalon.setD(D_LOW_GEAR);
			leftTalon.setProfile(Gear.HIGH_PROFILE);
			leftTalon.setF(F_HIGH_GEAR);
			leftTalon.setP(P_HIGH_GEAR);
			leftTalon.setI(I_HIGH_GEAR);
			leftTalon.setD(D_HIGH_GEAR);
			leftTalon.setProfile(getCurrentGearProfile());
			leftTalon.configEncoderCodesPerRev(TICKS_PER_REV);
			leftTalon.enableBrakeMode(true);
			leftTalon.setEncPosition(0);
			leftTalon.reverseSensor(false);
			leftTalon.enable();

			leftEncoder = new TalonEncoder(leftTalon);

			tempLeftTalon = new CANTalon(RobotMap.DRIVE_LEFT_B_TALON_PORT);
			tempLeftTalon.changeControlMode(TalonControlMode.Follower);
			tempLeftTalon.set(leftTalon.getDeviceID());
			tempLeftTalon.enableBrakeMode(true);

			rightTalon = new CANTalon(RobotMap.DRIVE_RIGHT_F_TALON_PORT);

			if (EnabledSubsystems.DRIVE_DUMB_ENABLED) {
				rightTalon.changeControlMode(TalonControlMode.PercentVbus);
			} else {
				rightTalon.changeControlMode(TalonControlMode.Speed);
			}
			rightTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			rightTalon.setProfile(Gear.LOW_PROFILE);
			rightTalon.setF(F_LOW_GEAR);
			rightTalon.setP(P_LOW_GEAR);
			rightTalon.setI(I_LOW_GEAR);
			rightTalon.setD(D_LOW_GEAR);
			rightTalon.setProfile(Gear.HIGH_PROFILE);
			rightTalon.setF(F_HIGH_GEAR);
			rightTalon.setP(P_HIGH_GEAR);
			rightTalon.setI(I_HIGH_GEAR);
			rightTalon.setD(D_HIGH_GEAR);
			rightTalon.setProfile(getCurrentGearProfile());
			rightTalon.configEncoderCodesPerRev(TICKS_PER_REV);
			rightTalon.enableBrakeMode(true);
			rightTalon.setEncPosition(0);
			rightTalon.reverseSensor(true);
			rightTalon.setInverted(true);
			rightTalon.enable();

			rightEncoder = new TalonEncoder(rightTalon);

			tempRightTalon = new CANTalon(RobotMap.DRIVE_RIGHT_B_TALON_PORT);
			tempRightTalon.changeControlMode(TalonControlMode.Follower);
			tempRightTalon.set(rightTalon.getDeviceID());
			tempRightTalon.enableBrakeMode(true);
		}
	}

	public static Drive getInstance() {
		if (singleton == null)
			init();
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new Drive();
			singleton.setJoystickCommand(new DriveJoystickCommand());
		}
	}

	/**
	 * Sets left and right motor speeds to the speeds needed for the given move
	 * and turn values
	 * 
	 * @param move
	 *            The speed, from -1 to 1 (inclusive), that the robot should go
	 *            at. 1 is max forward, 0 is stopped, -1 is max backward
	 * @param turn
	 *            The speed, from -1 to 1 (inclusive), that the robot should
	 *            turn at. 1 is max right, 0 is stopped, -1 is max left
	 * 
	 */
	public void arcadeDrive(double move, double turn) {
		move = NRMath.limit(move);
		turn = NRMath.limit(turn);
		double leftMotorSpeed, rightMotorSpeed;

		if (move > 0.0) {
			if (turn > 0.0) {
				leftMotorSpeed = move - turn;
				rightMotorSpeed = Math.max(move, turn);
			} else {
				leftMotorSpeed = Math.max(move, -turn);
				rightMotorSpeed = move + turn;
			}
		} else {
			if (turn > 0.0) {
				leftMotorSpeed = -Math.max(-move, turn);
				rightMotorSpeed = move + turn;
			} else {
				leftMotorSpeed = move - turn;
				rightMotorSpeed = -Math.max(-move, -turn);
			}
		}

		tankDrive(leftMotorSpeed, rightMotorSpeed);
	}

	/**
	 * Sets both left and right motors to the given speed.
	 * 
	 * @param left
	 *            the left motor speed from -1 to 1
	 * @param right
	 *            the right motor speed from -1 to 1
	 */
	public void tankDrive(double left, double right) {
		setMotorSpeedInPercent(left, right);
	}

	/**
	 * Sets the motor speed for the left and right motors
	 * 
	 * @param left
	 *            the left motor speed, from -1 to 1
	 * 
	 * @param right
	 *            the right motor speed, from -1 to 1
	 */
	public void setMotorSpeedInPercent(double left, double right) {
		setMotorSpeed(currentMaxSpeed().mul(left), currentMaxSpeed().mul(right));
	}

	/**
	 * Sets the motor speed for the left and right motors
	 * 
	 * @param left
	 *            the left motor speed
	 * 
	 * @param right
	 *            the right motor speed
	 */
	public void setMotorSpeed(Speed left, Speed right) {
		if (leftTalon != null && rightTalon != null) {
			leftMotorSetpoint = left;
			rightMotorSetpoint = right;

			if (leftTalon.getControlMode() == TalonControlMode.PercentVbus) {
				leftTalon.set(leftMotorSetpoint.div(currentMaxSpeed()));
			} else {
				leftTalon.set(leftMotorSetpoint.get(Distance.Unit.DRIVE_ROTATION, Time.Unit.MINUTE));
			}
			if (rightTalon.getControlMode() == TalonControlMode.PercentVbus) {
				rightTalon.set(rightMotorSetpoint.div(currentMaxSpeed()));
			} else {
				rightTalon.set(rightMotorSetpoint.get(Distance.Unit.DRIVE_ROTATION, Time.Unit.MINUTE));
			}

		}
	}

	/**
	 * Gets the current position of the left talon
	 * 
	 * @return current position of the talon
	 */
	public Distance getLeftPosition() {
		if (leftTalon != null)
			return new Distance(leftTalon.getPosition(), Distance.Unit.DRIVE_ROTATION);
		return Distance.ZERO;
	}

	/**
	 * Gets the current position of the right talon
	 * 
	 * @return current position of the talon
	 */
	public Distance getRightPosition() {
		if (rightTalon != null)
			return new Distance(rightTalon.getPosition(), Distance.Unit.DRIVE_ROTATION);
		return Distance.ZERO;
	}

	/**
	 * Gets the historical position of the left talon
	 * 
	 * @param deltaTime
	 *            How long ago to look
	 * @return current position of talon
	 */
	public Distance getHistoricalLeftPosition(Time deltaTime) {
		if (leftEncoder != null)
			return new Distance(leftEncoder.getPosition(deltaTime), Distance.Unit.DRIVE_ROTATION);
		return Distance.ZERO;
	}

	/**
	 * Gets the historical position of the right talon
	 * 
	 * @param deltaTime
	 *            How long ago to look
	 * @return current position of the talon
	 */
	public Distance getHistoricalRightPosition(Time deltaTime) {
		if (rightEncoder != null)
			return new Distance(rightEncoder.getPosition(deltaTime), Distance.Unit.DRIVE_ROTATION);
		return Distance.ZERO;
	}

	/**
	 * Get the current rate of the left encoder.
	 * 
	 * @return The current speed of the encoder
	 */
	public Speed getLeftSpeed() {
		if (leftTalon != null)
			return new Speed(leftTalon.getSpeed(), Distance.Unit.DRIVE_ROTATION, Time.Unit.MINUTE);
		return Speed.ZERO;
	}

	/**
	 * Get the current rate of the right encoder.
	 * 
	 * @return The current speed of the encoder
	 */
	public Speed getRightSpeed() {
		if (rightTalon != null)
			return new Speed(rightTalon.getSpeed(), Distance.Unit.DRIVE_ROTATION, Time.Unit.MINUTE);
		return Speed.ZERO;
	}

	/**
	 * Get the historical rate of the left encoder.
	 * 
	 * @param deltaTime
	 *            How long ago to look
	 * @return The past speed of the encoder
	 */
	public Speed getHistoricalLeftSpeed(Time deltaTime) {
		if (leftEncoder != null)
			return new Speed(leftEncoder.getVelocity(deltaTime));
		return Speed.ZERO;
	}

	/**
	 * Get the historical rate of the right encoder.
	 * 
	 * @param deltaTime
	 *            How long ago to look
	 * @return The past speed of the encoder
	 */
	public Speed getHistoricalRightSpeed(Time deltaTime) {
		if (rightEncoder != null)
			return new Speed(rightEncoder.getVelocity(deltaTime));
		return Speed.ZERO;
	}

	/**
	 * Sets the PID values for both talons for the given gear.
	 * 
	 * If izone or closed loop ramp rate are being used, this sets them to zero.
	 * 
	 * @param p
	 *            Corrects for errors in velocity
	 * @param i
	 *            Integral error
	 * @param d
	 *            Smooths corrections
	 * @param f
	 *            Feed forward gain
	 * @param gear
	 *            Which gear to use (high or low)
	 */
	public void setPID(double p, double i, double d, double f, Gear gear) {
		if (gear == Gear.high) {
			if (leftTalon != null)
				leftTalon.setPID(p, i, d, f, 0, 0, Gear.HIGH_PROFILE);
			if (rightTalon != null)
				rightTalon.setPID(p, i, d, f, 0, 0, Gear.HIGH_PROFILE);
		} else {
			if (leftTalon != null)
				leftTalon.setPID(p, i, d, f, 0, 0, Gear.LOW_PROFILE);
			if (rightTalon != null)
				rightTalon.setPID(p, i, d, f, 0, 0, Gear.LOW_PROFILE);
		}
	}

	/**
	 * Sets the current talon profile
	 * 
	 * @param profile
	 */
	private void setProfile(int profile) {
		if (leftTalon != null)
			leftTalon.setProfile(profile);
		if (rightTalon != null)
			rightTalon.setProfile(profile);
	}

	// GEAR SWITCHING

	public void switchToHighGear() {
		if (getCurrentGear() != Gear.high) {
			setProfile(Gear.HIGH_PROFILE);
			if (gearSwitcher != null) {
				gearSwitcher.set(Gear.HIGH_VALUE);
			}
		}
	}

	public void switchToLowGear() {
		if (getCurrentGear() != Gear.low) {
			setProfile(Gear.LOW_PROFILE);
			if (gearSwitcher != null) {
				gearSwitcher.set(Gear.LOW_VALUE);
			}
		}
	}

	public Gear getCurrentGear() {
		if (gearSwitcher != null) {
			if (gearSwitcher.get() == Gear.HIGH_VALUE) {
				return Gear.high;
			} else {
				return Gear.low;
			}
		} else {
			return Gear.low;
		}
	}

	public void switchGear() {
		if (getCurrentGear() == Gear.low) {
			switchToHighGear();
		} else {
			switchToLowGear();
		}
	}

	// END GEAR SWITCHING

	// DUMB DRIVE

	public void startDumbDrive() {
		if (leftTalon != null && rightTalon != null) {
			if (rightTalon.getControlMode() != TalonControlMode.PercentVbus) {
				rightTalon.changeControlMode(TalonControlMode.PercentVbus);
			}
			if (leftTalon.getControlMode() != TalonControlMode.PercentVbus) {
				leftTalon.changeControlMode(TalonControlMode.PercentVbus);
			}
		}
	}

	public void endDumbDrive() {
		if (leftTalon != null && rightTalon != null) {
			if (rightTalon.getControlMode() != TalonControlMode.Speed) {
				rightTalon.changeControlMode(TalonControlMode.Speed);
			}
			if (leftTalon.getControlMode() != TalonControlMode.Speed) {
				leftTalon.changeControlMode(TalonControlMode.Speed);
			}
		}
	}

	// END DUMB DRIVE

	/**
	 * Function that is periodically called once the Drive class is initialized
	 */
	@Override
	public void periodic() {

	}

	/**
	 * Sends data to SmartDashboard
	 */
	@Override
	public void smartDashboardInfo() {
		if (leftTalon != null && rightTalon != null) {
			if (EnabledSubsystems.DRIVE_SMARTDASHBOARD_BASIC_ENABLED) {
				SmartDashboard.putString("Drive Current", getLeftCurrent() + " : " + getRightCurrent());
				SmartDashboard.putString("Drive Left Speed", getLeftSpeed().get(Distance.Unit.FOOT, Time.Unit.SECOND) + " : " + leftMotorSetpoint.get(Distance.Unit.FOOT, Time.Unit.SECOND));
				SmartDashboard.putString("Drive Right Speed", getRightSpeed().get(Distance.Unit.FOOT, Time.Unit.SECOND) + " : " + rightMotorSetpoint.get(Distance.Unit.FOOT, Time.Unit.SECOND));
			}
			if (EnabledSubsystems.DRIVE_SMARTDASHBOARD_COMPLEX_ENABLED) {
				SmartDashboard.putData(this);
				SmartDashboard.putString("Drive Voltage",
						leftTalon.getOutputVoltage() + " : " + rightTalon.getOutputVoltage());
				SmartDashboard.putNumber("Drive Left Position", getLeftPosition().get(Distance.Unit.FOOT));
				SmartDashboard.putNumber("Drive Right Position", getRightPosition().get(Distance.Unit.FOOT));
				SmartDashboard.putString("Current Drive Gear", getCurrentGear().toString());
			}
		}
	}

	/**
	 * What subsystem does upon robot being disabled
	 */
	@Override
	public void disable() {
		setMotorSpeedInPercent(0, 0);
	}

	public double getRightCurrent() {
		if (rightTalon != null) {
			return rightTalon.getOutputCurrent();
		}
		return 0;
	}

	public double getLeftCurrent() {
		if (leftTalon != null) {
			return leftTalon.getOutputCurrent();
		}
		return 0;
	}

	public void setVoltageRampRate(double rampRate) {
		if(rightTalon != null) {
			rightTalon.setVoltageRampRate(rampRate);
		} 
		if(tempRightTalon != null) {
			tempRightTalon.setVoltageRampRate(rampRate);
		}
		if(leftTalon != null) {
			leftTalon.setVoltageRampRate(rampRate);
		}
		if(tempLeftTalon != null) {
			tempLeftTalon.setVoltageRampRate(rampRate);
		}
	}

	// PID SOURCE

	private PIDSourceType type = PIDSourceType.kRate;

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		type = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return type;
	}

	@Override
	public double pidGetLeft() {
		if (type == PIDSourceType.kRate) {
			return getInstance().getLeftSpeed().get(Distance.Unit.DRIVE_ROTATION, Time.Unit.MINUTE);
		} else {
			return getInstance().getLeftPosition().get(Distance.Unit.DRIVE_ROTATION);
		}
	}

	@Override
	public double pidGetRight() {
		if (type == PIDSourceType.kRate) {
			return getInstance().getRightSpeed().get(Distance.Unit.DRIVE_ROTATION, Time.Unit.MINUTE);
		} else {
			return getInstance().getRightPosition().get(Distance.Unit.DRIVE_ROTATION);
		}
	}

	// PID OUTPUT

	@Override
	public void pidWrite(double outputLeft, double outputRight) {
		setMotorSpeed(new Speed(outputLeft, Distance.Unit.DRIVE_ROTATION, Time.Unit.MINUTE),
				new Speed(outputRight, Distance.Unit.DRIVE_ROTATION, Time.Unit.MINUTE));
	}

	// END OF PID

}
