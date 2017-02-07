package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.HistoricalCANTalon;
import edu.nr.lib.NRMath;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;

import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends NRSubsystem {

	private static Drive singleton;

	private HistoricalCANTalon leftTalon, rightTalon, tempLeftTalon, tempRightTalon;

	private static final double WHEEL_DIAMETER = (4.0 / 12.0); // Measured in feet
	private static final double DISTANCE_PER_REV = Math.PI * WHEEL_DIAMETER;
	private static final double MAX_RPM = RobotMap.MAX_DRIVE_SPEED / DISTANCE_PER_REV * 60;

	private static final int TICKS_PER_REV = 256; //TODO: Drive: Get ticks per revolution
	private static final int NATIVE_UNITS_PER_REV = 4*TICKS_PER_REV;

	double leftMotorSetpoint = 0;
	double rightMotorSetpoint = 0;

	//TODO: Drive: Find FPID values
	public static final double F = (MAX_RPM / RobotMap.HUNDRED_MS_PER_MIN * NATIVE_UNITS_PER_REV);
	public static final double P = 0;
	public static final double I = 0;
	public static final double D = 0;
	
	public static enum driveMode {
		arcadeDrive, tankDrive
	}
	
	private Drive() {
		//TODO: Drive: Find phase of motors
		
		if (EnabledSubsystems.DRIVE_ENABLED) {
			leftTalon = new HistoricalCANTalon(RobotMap.TALON_LEFT_F);

			leftTalon.changeControlMode(TalonControlMode.PercentVbus);
			leftTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			leftTalon.setF(F);
			leftTalon.setP(P);
			leftTalon.setI(I);
			leftTalon.setD(D);
			leftTalon.configEncoderCodesPerRev(TICKS_PER_REV);
			leftTalon.enableBrakeMode(true);
			leftTalon.setEncPosition(0);
			leftTalon.reverseSensor(false);
			leftTalon.enable();

			tempLeftTalon = new HistoricalCANTalon(RobotMap.TALON_LEFT_B);
			tempLeftTalon.changeControlMode(TalonControlMode.Follower);
			tempLeftTalon.set(leftTalon.getDeviceID());
			tempLeftTalon.enableBrakeMode(true);

			rightTalon = new HistoricalCANTalon(RobotMap.TALON_RIGHT_F);

			rightTalon.changeControlMode(TalonControlMode.PercentVbus);
			rightTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			rightTalon.setF(F);
			rightTalon.setP(P);
			rightTalon.setI(I);
			rightTalon.setD(D);
			rightTalon.configEncoderCodesPerRev(TICKS_PER_REV);
			rightTalon.enableBrakeMode(true);
			rightTalon.setEncPosition(0);
			rightTalon.reverseSensor(false);
			rightTalon.enable();

			tempRightTalon = new HistoricalCANTalon(RobotMap.TALON_RIGHT_B);
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
	 * Get the distance the left encoder has driven since the last reset
	 * 
	 * @return The distance the left encoder has driven since the last reset.
	 */
	public double getEncoderLeftDistance() {
		if (leftTalon != null)
			return leftTalon.getEncPosition() / 4;
		return 0;
	}

	/**
	 * Get the distance the right encoder has driven since the last reset
	 * 
	 * @return The distance the right encoder has driven since the last reset as
	 *         scaled by the value from setDistancePerPulse().
	 */
	public double getEncoderRightDistance() {
		if (rightTalon != null)
			return rightTalon.getEncPosition() / 4;
		return 0;
	}

	/**
	 * Get the current rate of the left encoder. Units are distance per second
	 * as scaled by the value from setDistancePerPulse().
	 * 
	 * @return The current rate of the encoder
	 */
	public double getEncoderLeftSpeed() {
		if (leftTalon != null)
			return leftTalon.getEncVelocity() / 4;
		return 0;
	}

	/**
	 * Get the current rate of the right encoder. Units are distance per second
	 * as scaled by the value from setDistancePerPulse().
	 * 
	 * @return The current rate of the encoder
	 */
	public double getEncoderRightSpeed() {
		if (rightTalon != null)
			return rightTalon.getEncVelocity() / 4;
		return 0;
	}

	/**
	 * Gets the average distance of the encoders
	 * 
	 * @return The average distance the encoders have driven since the last
	 *         reset as scaled by the value from setDistancePerPulse().
	 */
	public double getEncoderAverageDistance() {
		return (getEncoderLeftDistance() + getEncoderRightDistance()) / 2;
	}

	/**
	 * Get the average current rate of the encoders. Units are distance per
	 * second as scaled by the value from setDistancePerPulse().
	 * 
	 * @return The current average rate of the encoders
	 */
	public double getEncoderAverageSpeed() {
		return (getEncoderRightSpeed() + getEncoderLeftSpeed()) / 2;
	}

	/**
	 * Sets the PID values for both talons
	 * 
	 * @param p Corrects for errors in velocity
	 * @param i Integral error
	 * @param d Smooths corrections
	 * @param f Feed forward gain
	 */
	public void setPID(double p, double i, double d, double f) {
		if (leftTalon != null)
			leftTalon.setPID(p, i, d, f, 0, 0, 0);
		if (rightTalon != null)
			rightTalon.setPID(p, i, d, f, 0, 0, 0);
	}

	/**
	 * Resets talons
	 */
	public void resetTalons() {
		if (leftTalon != null)
			leftTalon.clearIAccum();
		if (rightTalon != null)
			rightTalon.clearIAccum();
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
			SmartDashboard.putString("Drive Current", leftTalon.getOutputCurrent() + " : " + rightTalon.getOutputCurrent());
			SmartDashboard.putString("Drive Voltage", leftTalon.getOutputVoltage() + " : " + rightTalon.getOutputVoltage());
			SmartDashboard.putString("Drive Left Speed", leftTalon.getSpeed() + " : " + getInstance().leftMotorSetpoint);
			SmartDashboard.putString("Drive Right Speed", rightTalon.getSpeed() + " : " + getInstance().rightMotorSetpoint);
		}
	}

	/**
	 * What subsystem does upon robot being disabled
	 */
	@Override
	public void disable() {
		setMotorSpeed(0, 0);
	}

}
