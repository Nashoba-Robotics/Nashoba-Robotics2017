package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRMath;
import edu.nr.lib.NRSubsystem;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * Class that controls talons, encoders, and type of drive
 *
 */
public class Drive extends NRSubsystem implements SmartDashboardSource, Periodic {

	private static Drive singleton;

	private CANTalon leftTalon, rightTalon, tempLeftTalon, tempRightTalon;

	// TODO: See if all below are needed this year:
	// private static final int ticksPerRev = ();
	// private static final double wheelDiameter = (); //Measured in feet
	// private static final double distancePerRev = Math.PI * wheelDiameter;

	// private static final double rpm = RobotMap.MAX_SPEED / distancePerRev *
	// 60;

	// private static final double hundredMSPerMin = ;
	// private static final int nativeUnitsPerRev = 4 * ticksPerRev;

	// TODO: Confirm actual setpoints are 0
	double leftMotorSetpoint = 0;
	double rightMotorSetpoint = 0;

	// public static final double turn_F = (rpm / hundredMSPerMin *
	// nativeUnitsPerRev);
	// public static final double turn_P = 0;
	// public static final double turn_I = 0;
	// public static final double turn_D = 0;

	private Drive() {
		super(new DriveJoystickCommand(OI.getInstance().getLeftDriveStick(), OI.getInstance().getRightDriveStick()));
		if (EnabledSubsystems.leftDriveEnabled) {
			leftTalon = new CANTalon(RobotMap.TALON_LEFT_A);

			leftTalon.changeControlMode(TalonControlMode.PercentVbus);
			// TODO: Confirm actual feedback device
			// leftTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			// leftTalon.setF(turn_F);
			// leftTalon.setP(turn_P);
			// leftTalon.setI(turn_I);
			// leftTalon.setD(turn_D);
			// leftTalon.configEncoderCodesPerRev(ticksPerRev);
			// TODO: Confirm initial brake mode
			// leftTalon.enableBrakeMode(false);
			leftTalon.setEncPosition(0);
			// TODO: Determine reverseSensor
			// leftTalon.reverseSensor(true);
			leftTalon.enable();

			tempLeftTalon = new CANTalon(RobotMap.TALON_LEFT_B);
			tempLeftTalon.changeControlMode(TalonControlMode.PercentVbus);
			tempLeftTalon.set(leftTalon.getDeviceID());
			// TODO: Determine initial brake mode
			// tempLeftTalon.enableBrakeMode(false);
		}
		if (EnabledSubsystems.rightDriveEnabled) {
			rightTalon = new CANTalon(RobotMap.TALON_RIGHT_A);

			rightTalon.changeControlMode(TalonControlMode.PercentVbus);
			// TODO: Confirm actual feedback device
			// rightTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			// rightTalon.setF(turn_F);
			// rightTalon.setP(turn_P);
			// rightTalon.setI(turn_I);
			// rightTalon.setD(turn_D);
			// rightTalon.configEncoderCodesPerRev(ticksPerRev);
			// TODO: Confirm initial brake mode
			// rightTalon.enableBrakeMode(false);
			rightTalon.setEncPosition(0);
			// TODO: Determine reverseSensor
			// rightTalon.reverseSensor(true);
			rightTalon.enable();

			tempRightTalon = new CANTalon(RobotMap.TALON_RIGHT_B);
			tempRightTalon.changeControlMode(TalonControlMode.PercentVbus);
			tempRightTalon.set(rightTalon.getDeviceID());
			// TODO: Determine initial brake mode
			// temprightTalon.enableBrakeMode(false);
		}
	}

	public static Drive getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new Drive();
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
	 */
	public void arcadeDrive(double move, double turn) {
		// TODO: Determine if speed multiplier will be used
		// arcadeDrive(move, turn, false);
	}

	/**
	 * Sets left and right motor speeds to the speeds needed for the given move
	 * and turn values, multiplied by the OI speed multiplier if the speed
	 * multiplier parameter is true. If you don't care about the speed
	 * multiplier parameter, you might want to use {@link arcadeDrive(double
	 * move, double turn)}
	 * 
	 * @param move
	 *            The speed, from -1 to 1 (inclusive), that the robot should go
	 *            at. 1 is max forward, 0 is stopped, -1 is max backward
	 * @param turn
	 *            The speed, from -1 to 1 (inclusive), that the robot should
	 *            turn at. 1 is max right, 0 is stopped, -1 is max left
	 * @param speedMultiplier
	 *            whether or not to use the OI speed multiplier It should really
	 *            only be used for operator driving
	 * 
	 */
	public void arcadeDrive(double move, double turn, boolean speedMultiplier) {
		move = NRMath.limit(move);
		turn = NRMath.limit(turn);
		double leftMotorSpeed, rightMotorSpeed;
		rightMotorSpeed = leftMotorSpeed = move;
		leftMotorSpeed += turn;
		rightMotorSpeed -= turn;

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

		double multiplier = speedMultiplier ? OI.getInstance().driveSpeedMultiplier : 1;
		tankDrive(leftMotorSpeed * multiplier, rightMotorSpeed * multiplier);
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
	 * @param right
	 *            the right motor speed, from -1 to 1
	 */
	public void setMotorSpeed(double left, double right) {
		leftMotorSetpoint = left;// * rpm;
		rightMotorSetpoint = right;// * rpm;

		if (leftTalon != null) {
			if (leftTalon.getControlMode() == TalonControlMode.Speed)
				leftTalon.set(left/* \*rpm */);
			else
				leftTalon.set(left);
		}
		if (rightTalon != null) {
			if (rightTalon.getControlMode() == TalonControlMode.Speed)
				rightTalon.set(right/* \*rpm */);
			else
				rightTalon.set(right);
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
			return -leftTalon.getEncPosition() / 4;
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
			return -leftTalon.getEncVelocity() / 4;
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
	 * @param p
	 * @param i
	 * @param d
	 * @param f
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

	}

	@Override
	public void disable() {

	}

	/**
	 * Chooses the default command when the Drive class is initialized
	 */
	@Override
	protected void initDefaultCommand() {
		// TODO: Choose default command
	}

}
