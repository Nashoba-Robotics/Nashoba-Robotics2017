package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRMath;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.interfaces.DoublePIDOutput;
import edu.nr.lib.interfaces.DoublePIDSource;
import edu.nr.lib.sensorhistory.sf2.HistoricalCANTalon;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;

import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends NRSubsystem implements DoublePIDOutput, DoublePIDSource {

	private static Drive singleton;

	private HistoricalCANTalon leftTalon, rightTalon, tempLeftTalon, tempRightTalon;
	
	/**
	 * The distance the wheel travels in a single revolution, in feet
	 * 
	 * This is equivalent to the circumference of the wheel
	 */
	private static final double DISTANCE_PER_REV = Math.PI * RobotMap.DRIVE_WHEEL_DIAMETER;
	
	/**
	 * The maximum speed of the robot in low gear, in rotations per minute
	 */
	private static final double MAX_LOW_GEAR_RPM = RobotMap.MAX_DRIVE_LOW_GEAR_SPEED / DISTANCE_PER_REV * RobotMap.SECONDS_PER_MINUTE;

	/**
	 * The maximum speed of the robot in high gear, in rotations per minute
	 */
	private static final double MAX_HIGH_GEAR_RPM = RobotMap.MAX_DRIVE_HIGH_GEAR_SPEED / DISTANCE_PER_REV * RobotMap.SECONDS_PER_MINUTE;

	/**
	 * The number of encoder ticks per wheel revolution
	 */
	static final int TICKS_PER_REV = 256; //TODO: Drive: Get ticks per revolution
	
	/**
	 * The number of CANTalon "Native Units" per revolution
	 */
	private static final int NATIVE_UNITS_PER_REV = 4 * TICKS_PER_REV;

	/**
	 * The speed that the left drivetrain is currently supposed to be running at, in rotations per minute
	 */
	double leftMotorSetpoint = 0;
	
	/**
	 * The speed that the right drivetrain is currently supposed to be running at, in rotations per minute
	 */
	double rightMotorSetpoint = 0;

	//TODO: Drive: Find FPID values
	public static final double F_LOW_GEAR = (MAX_LOW_GEAR_RPM / RobotMap.HUNDRED_MS_PER_MIN * NATIVE_UNITS_PER_REV);
	public static final double P_LOW_GEAR = 0;
	public static final double I_LOW_GEAR = 0;
	public static final double D_LOW_GEAR = 0;

	//TODO: Drive: Find FPID values
	public static final double F_HIGH_GEAR = (MAX_HIGH_GEAR_RPM / RobotMap.HUNDRED_MS_PER_MIN * NATIVE_UNITS_PER_REV);
	public static final double P_HIGH_GEAR = 0;
	public static final double I_HIGH_GEAR = 0;
	public static final double D_HIGH_GEAR = 0;
	
	public static enum DriveMode {
		arcadeDrive, tankDrive
	}
	
	public static enum Gear {
		high, low
	}

	private static int HIGH_GEAR_PROFILE = 0;
	private static int LOW_GEAR_PROFILE = 1;
	
	private static int DEFAULT_PROFILE = LOW_GEAR_PROFILE; //TODO: Drive: What should the default gearing be?
	
	private Gear currentGear = Gear.low;
	
	PIDSourceType type = PIDSourceType.kRate;
	
	private Drive() {
		//TODO: Drive: Find phase of motors
		
		if (EnabledSubsystems.DRIVE_ENABLED) {
			leftTalon = new HistoricalCANTalon(RobotMap.DRIVE_LEFT_F_TALON_PORT);

			leftTalon.changeControlMode(TalonControlMode.PercentVbus);
			leftTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			leftTalon.setProfile(LOW_GEAR_PROFILE);
			leftTalon.setF(F_LOW_GEAR);
			leftTalon.setP(P_LOW_GEAR);
			leftTalon.setI(I_LOW_GEAR);
			leftTalon.setD(D_LOW_GEAR);
			leftTalon.setProfile(HIGH_GEAR_PROFILE);
			leftTalon.setF(F_HIGH_GEAR);
			leftTalon.setP(P_HIGH_GEAR);
			leftTalon.setI(I_HIGH_GEAR);
			leftTalon.setD(D_HIGH_GEAR);
			leftTalon.setProfile(DEFAULT_PROFILE);
			leftTalon.configEncoderCodesPerRev(TICKS_PER_REV);
			leftTalon.enableBrakeMode(true);
			leftTalon.setEncPosition(0);
			leftTalon.reverseSensor(false);
			leftTalon.enable();

			tempLeftTalon = new HistoricalCANTalon(RobotMap.DRIVE_LEFT_B_TALON_PORT);
			tempLeftTalon.changeControlMode(TalonControlMode.Follower);
			tempLeftTalon.set(leftTalon.getDeviceID());
			tempLeftTalon.enableBrakeMode(true);

			rightTalon = new HistoricalCANTalon(RobotMap.DRIVE_RIGHT_F_TALON_PORT);

			rightTalon.changeControlMode(TalonControlMode.PercentVbus);
			rightTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			rightTalon.setProfile(LOW_GEAR_PROFILE);
			rightTalon.setF(F_LOW_GEAR);
			rightTalon.setP(P_LOW_GEAR);
			rightTalon.setI(I_LOW_GEAR);
			rightTalon.setD(D_LOW_GEAR);
			rightTalon.setProfile(HIGH_GEAR_PROFILE);
			rightTalon.setF(F_HIGH_GEAR);
			rightTalon.setP(P_HIGH_GEAR);
			rightTalon.setI(I_HIGH_GEAR);
			rightTalon.setD(D_HIGH_GEAR);
			rightTalon.setProfile(DEFAULT_PROFILE);
			rightTalon.configEncoderCodesPerRev(TICKS_PER_REV);
			rightTalon.enableBrakeMode(true);
			rightTalon.setEncPosition(0);
			rightTalon.reverseSensor(false);
			rightTalon.enable();

			tempRightTalon = new HistoricalCANTalon(RobotMap.DRIVE_RIGHT_B_TALON_PORT);
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
	 * Set the voltage ramp rate for both drive talons. Limits the rate at which
	 * the throttle will change.
	 * 
	 * @param rampRate
	 *            Maximum change in voltage, in volts / sec.
	 */
	public void setTalonRampRate(double rampRate) {
		if (leftTalon != null)
			leftTalon.setVoltageRampRate(rampRate);
		if (rightTalon != null)
			rightTalon.setVoltageRampRate(rampRate);
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
	 *            the left motor speed
	 * @param right
	 *            the right motor speed
	 */
	public void tankDrive(double left, double right) {
		setMotorSpeed(left, right);
	}

	/**
	 * Sets the motor speed for the left and right motors
	 * 
	 * @param left
	 *            the left motor speed, from -1 to 1
	 *            		OR
	 *            the left motor speed in rpm
	 *         
	 * @param right
	 *            the right motor speed, from -1 to 1
	 *            		OR
	 *            the right motor speed in rpm
	 */
	public void setMotorSpeed(double left, double right) {
		leftMotorSetpoint = left * RobotMap.LEFT_DRIVE_DIRECTION;;
		rightMotorSetpoint = right * RobotMap.RIGHT_DRIVE_DIRECTION;;

		if (leftTalon != null && rightTalon != null) {
			leftTalon.set(leftMotorSetpoint);
			rightTalon.set(rightMotorSetpoint);
		}
	}

	/**
	 * Gets whether the PIDs are enabled or not. If both are enabled, then
	 * returns true, otherwise returns false
	 * 
	 * @return whether the PIDs are enabled
	 */
	public boolean getPIDEnabled() {
		if (leftTalon != null && rightTalon != null)
			return leftTalon.getControlMode() == TalonControlMode.Speed
					&& rightTalon.getControlMode() == TalonControlMode.Speed;
		return false;
	}

	/**
	 * Enables or disables both left and right PIDs. Disabling also resets the
	 * integral term and the previous error of the PID, and sets the output to
	 * zero
	 * 
	 * Doesn't do anything if they are already that state.
	 * 
	 * @param enabled
	 *            whether to enable (true) or disable (false)
	 */
	public void setPIDEnabled(boolean enabled) {
		if (leftTalon != null && rightTalon != null) {
			if (enabled) {
				if (!getPIDEnabled()) {
					leftTalon.changeControlMode(TalonControlMode.Speed);
					rightTalon.changeControlMode(TalonControlMode.Speed);
				}
			} else if (getPIDEnabled()) {
				leftTalon.clearIAccum();
				rightTalon.clearIAccum();
				leftTalon.changeControlMode(TalonControlMode.PercentVbus);
				rightTalon.changeControlMode(TalonControlMode.PercentVbus);
			}
		}
	}

	/**
	 * Resets both the left and right encoders
	 */
	public void resetEncoders() {
		if (leftTalon != null)
			leftTalon.setPosition(0);
		if (rightTalon != null)
			rightTalon.setPosition(0);
	}

	/**
	 * Gets the current position of the talon
	 * 
	 * @return current position of talon
	 */
	public double getLeftPosition() {
		if(leftTalon != null)
			return leftTalon.getPosition();
		return 0;
	}
	
	public double getHistoricalLeftPosition(double deltaTime) {
		if (leftTalon != null)
			return leftTalon.getHistoricalPosition(deltaTime);
		return 0;
	}
	
	/**
	 * Gets the current position of the talon
	 * 
	 * @return current position of talon
	 */
	public double getRightPosition() {
		if(rightTalon != null)
			return rightTalon.getPosition();
		return 0;
	}
	
	public double getHistoricalRightPosition(double deltaTime) {
		if (rightTalon != null)
			return rightTalon.getHistoricalPosition(deltaTime);
		return 0;
	}
	
	/**
	 * Get the distance the left encoder has driven since the last reset
	 * 
	 * @return The distance the left encoder has driven since the last reset, in rotations.
	 */
	public double getEncoderLeftDistance() {
		if (leftTalon != null)
			return leftTalon.getPosition();
		return 0;
	}

	/**
	 * Get the distance the right encoder has driven since the last reset
	 * 
	 * @return The distance the right encoder has driven since the last reset, in rotations.
	 */
	public double getEncoderRightDistance() {
		if (rightTalon != null)
			return rightTalon.getPosition();
		return 0;
	}

	/**
	 * Get the current rate of the left encoder. Units are rotations per minute
	 * 
	 * @return The current speed of the encoder
	 */
	public double getEncoderLeftSpeed() {
		if (leftTalon != null)
			return leftTalon.getSpeed();
		return 0;
	}

	/**
	 * Get the current rate of the right encoder. Units are rotations per minute
	 * 
	 * @return The current speed of the encoder
	 */
	public double getEncoderRightSpeed() {
		if (rightTalon != null)
			return rightTalon.getSpeed();
		return 0;
	}

	/**
	 * Gets the average distance of the encoders
	 * 
	 * @return The average distance the encoders have driven since the last
	 *         reset in rotations.
	 */
	public double getEncoderAverageDistance() {
		return (getEncoderLeftDistance() + getEncoderRightDistance()) / 2;
	}

	/**
	 * Get the average current rate of the encoders. Units are rotations per minute
	 * 
	 * @return The current average rate of the encoders
	 */
	public double getEncoderAverageSpeed() {
		return (getEncoderRightSpeed() + getEncoderLeftSpeed()) / 2;
	}

	/**
	 * Sets the PID values for both talons for the given gear.
	 * 
	 * If izone or closed loop ramp rate are being used, this sets them to zero.
	 * 
	 * @param p Corrects for errors in velocity
	 * @param i Integral error
	 * @param d Smooths corrections
	 * @param f Feed forward gain
	 * @param gear Which gear to use (high or low)
	 */
	public void setPID(double p, double i, double d, double f, Gear gear) {
		if(gear == Gear.high) {
			if (leftTalon != null) 
				leftTalon.setPID(p, i, d, f, 0, 0, HIGH_GEAR_PROFILE);
			if (rightTalon != null)
				rightTalon.setPID(p, i, d, f, 0, 0, HIGH_GEAR_PROFILE);
		} else {
			if (leftTalon != null)
				leftTalon.setPID(p, i, d, f, 0, 0, LOW_GEAR_PROFILE);
			if (rightTalon != null)
				rightTalon.setPID(p, i, d, f, 0, 0, LOW_GEAR_PROFILE);
		}
	}

	/**
	 * Resets talon integral accumulation
	 */
	public void resetTalons() {
		if (leftTalon != null)
			leftTalon.clearIAccum();
		if (rightTalon != null)
			rightTalon.clearIAccum();
	}
	
	/**
	 * Sets the current talon profile
	 * @param profile
	 */
	private void setProfile(int profile) {
		if(leftTalon != null) 
			leftTalon.setProfile(profile);
		if(rightTalon != null)
			rightTalon.setProfile(profile);
	}
	
	public void switchToHighGear() {
		//TODO: Drive: Switch to high gear
		
		if(currentGear != Gear.high) {
			setProfile(HIGH_GEAR_PROFILE);
			currentGear = Gear.high;
		}
	}
	
	public void switchToLowGear() {
		//TODO: Drive: Switch to low gear
		
		if(currentGear != Gear.low) {
			setProfile(LOW_GEAR_PROFILE);
			currentGear = Gear.low;
		}
	}
	
	public Gear getCurrentGear() {
		return currentGear;
	}
	
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
			if(EnabledSubsystems.DRIVE_SMARTDASHBOARD_BASIC_ENABLED) {
				SmartDashboard.putString("Drive Current", leftTalon.getOutputCurrent() + " : " + rightTalon.getOutputCurrent());
				SmartDashboard.putString("Drive Left Speed", leftTalon.getSpeed() + " : " + getInstance().leftMotorSetpoint);
				SmartDashboard.putString("Drive Right Speed", rightTalon.getSpeed() + " : " + getInstance().rightMotorSetpoint);
			}
			if(EnabledSubsystems.DRIVE_SMARTDASHBOARD_COMPLEX_ENABLED) {
				SmartDashboard.putString("Drive Voltage", leftTalon.getOutputVoltage() + " : " + rightTalon.getOutputVoltage());
			}
		}
	}

	/**
	 * What subsystem does upon robot being disabled
	 */
	@Override
	public void disable() {
		setMotorSpeed(0, 0);
	}

	public double getRightCurrent() {
		if(rightTalon != null) {
			return rightTalon.getOutputCurrent();
		} else {
			return 0;
		}
	}

	public double getLeftCurrent() {
		if(leftTalon != null) {
			return leftTalon.getOutputCurrent();
		} else {
			return 0;
		}
	}

	public double getAverageCurrent() {
		return (getLeftCurrent() + getRightCurrent())/2;
	}

	public void switchGear() {
		if(currentGear == Gear.low) {
			switchToHighGear();
		} else {
			switchToLowGear();
		}
	}

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
			return getInstance().getEncoderLeftSpeed() / RobotMap.SECONDS_PER_MINUTE;
		} else {
			return getInstance().getLeftPosition();
		}
	}

	@Override
	public double pidGetRight() {
		if (type == PIDSourceType.kRate) {
			return getInstance().getEncoderRightSpeed() / RobotMap.SECONDS_PER_MINUTE;
		} else {
			return getInstance().getRightPosition();
		}
	}

	@Override
	public void pidWrite(double outputLeft, double outputRight) {
		leftMotorSetpoint = outputLeft;
		rightMotorSetpoint = outputRight;

		if(currentGear == Gear.low) {
			leftTalon.set(leftMotorSetpoint * RobotMap.MAX_DRIVE_LOW_GEAR_SPEED);
			rightTalon.set(rightMotorSetpoint * RobotMap.MAX_DRIVE_LOW_GEAR_SPEED);
		} else {
			leftTalon.set(leftMotorSetpoint * RobotMap.MAX_DRIVE_HIGH_GEAR_SPEED);
			rightTalon.set(rightMotorSetpoint * RobotMap.MAX_DRIVE_HIGH_GEAR_SPEED);
		}
	}

}
