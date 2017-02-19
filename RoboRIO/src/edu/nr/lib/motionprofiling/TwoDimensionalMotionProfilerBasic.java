package edu.nr.lib.motionprofiling;

import java.util.Timer;
import java.util.TimerTask;

import edu.nr.lib.AngleGyroCorrection;
import edu.nr.lib.interfaces.DoublePIDOutput;
import edu.nr.lib.interfaces.DoublePIDSource;
import edu.nr.lib.interfaces.GyroCorrection;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TwoDimensionalMotionProfilerBasic extends TimerTask implements TwoDimensionalMotionProfiler  {

	private final Timer timer;
	
	//In milliseconds
	private final long period;
	private static final long defaultPeriod = 5; //200 Hz 
	
	private double prevTime;
	private double startTime;
	
	private boolean enabled = false;
	private DoublePIDOutput out;
	private DoublePIDSource source;
	
	private double ka, kp, kd, kv, kp_theta;
	private double errorLastLeft;
	private double errorLastRight;
	
	private double initialPositionLeft;
	private double initialPositionRight;
			
	private TwoDimensionalTrajectory trajectory;
	
	GyroCorrection gyroCorrection;

		
	public TwoDimensionalMotionProfilerBasic(DoublePIDOutput out, DoublePIDSource source, double kv, double ka, double kp, double kd, double kp_theta, long period) {
		this.out = out;
		this.source = source;
		this.period = period;
		this.trajectory = new TwoDimensionalTrajectoryBlank();
		timer = new Timer();
		timer.schedule(this, 0, this.period);
		this.source.setPIDSourceType(PIDSourceType.kDisplacement);
		this.ka = ka;
		this.kp = kp;
		this.kd = kd;
		this.kv = kv;
		this.kp_theta = kp_theta;
		this.initialPositionLeft = source.pidGetLeft();
		this.initialPositionRight = source.pidGetRight();
		this.gyroCorrection = new AngleGyroCorrection();
		reset();
	}
	
	public TwoDimensionalMotionProfilerBasic(DoublePIDOutput out, DoublePIDSource source, double kv, double ka, double kp, double kd, double kp_theta) {
		this(out, source, kv, ka, kp, kd, kp_theta, defaultPeriod);
	}
	
	double timeOfVChange = 0;
	double prevV;
	
	@Override
	public void run() {
		if(enabled) {
			double dt = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - prevTime;
			
			double currentTimeSinceStart = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime;
			
			double velocityGoalLeft = trajectory.getLeftGoalVelocity(currentTimeSinceStart);
			double positionGoalLeft = trajectory.getLeftGoalPosition(currentTimeSinceStart);
			double accelGoalLeft = trajectory.getLeftGoalAccel(currentTimeSinceStart);

			double velocityGoalRight = trajectory.getRightGoalVelocity(currentTimeSinceStart);
			double positionGoalRight = trajectory.getRightGoalPosition(currentTimeSinceStart);
			double accelGoalRight = trajectory.getRightGoalAccel(currentTimeSinceStart);

			double headingAdjustment = gyroCorrection.getTurnValue(kp_theta);
			
			double errorLeft = positionGoalLeft - source.pidGetLeft() + initialPositionLeft;			
			double errorDerivLeft = (errorLeft - errorLastLeft) / dt;
			double prelimOutputLeft = velocityGoalLeft * kv + accelGoalLeft * ka + errorLeft * kp + errorDerivLeft * kd;
			errorLastLeft = errorLeft;
			
			double outputLeft = 0;

			if (prelimOutputLeft > 0.0) {
				if (headingAdjustment > 0.0) {
					outputLeft = prelimOutputLeft - headingAdjustment;
				} else {
					outputLeft = Math.max(prelimOutputLeft, -headingAdjustment);
				}
			} else {
				if (headingAdjustment > 0.0) {
					outputLeft = -Math.max(-prelimOutputLeft, headingAdjustment);
				} else {
					outputLeft = prelimOutputLeft - headingAdjustment;
				}
			}
			
			double errorRight = positionGoalRight - source.pidGetRight() + initialPositionRight;			
			double errorDerivRight = (errorRight - errorLastRight) / dt;
			double prelimOutputRight = velocityGoalRight * kv + accelGoalRight * ka + errorRight * kp + errorDerivRight * kd;
			errorLastRight = errorRight;
			
			double outputRight = 0;
			
			if (prelimOutputRight > 0.0) {
				if (headingAdjustment > 0.0) {
					outputRight = Math.max(prelimOutputRight, headingAdjustment);
				} else {
					outputRight = prelimOutputRight + headingAdjustment;
				}
			} else {
				if (headingAdjustment > 0.0) {
					outputRight = prelimOutputRight + headingAdjustment;
				} else {
					outputRight = -Math.max(-prelimOutputRight, -headingAdjustment);
				}
			}
			
			out.pidWrite(outputLeft, outputRight);
			
			source.setPIDSourceType(PIDSourceType.kRate);
			SmartDashboard.putString("Motion Profiler V Left", source.pidGetLeft() + ":" + outputLeft * trajectory.getLeftMaxPossibleVelocity() * Math.signum(trajectory.getLeftMaxPossibleVelocity()));
			SmartDashboard.putString("Motion Profiler V Right", source.pidGetRight() + ":" + outputRight * trajectory.getRightMaxPossibleVelocity() * Math.signum(trajectory.getRightMaxPossibleVelocity()));
			source.setPIDSourceType(PIDSourceType.kDisplacement);
			SmartDashboard.putString("Motion Profiler X Left", source.pidGetLeft() + ":" + (positionGoalLeft + initialPositionLeft));
			SmartDashboard.putString("Motion Profiler X Right", source.pidGetRight() + ":" + (positionGoalRight + initialPositionRight));
		}
		
		prevTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
	}
		
	/**
	 * Stop the profiler from running and resets it
	 */
	public void disable() {
		enabled = false;
		reset();
	}
	
	/**
	 * Reset the profiler and start it running
	 */
	public void enable() {
		enabled = true;
		reset();
	}
	
	/**
	 * Reset the previous time to the current time.
	 * Doesn't disable the controller
	 */
	public void reset() {
		errorLastLeft = 0;
		errorLastRight = 0;
		startTime = prevTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
		PIDSourceType type = source.getPIDSourceType();
		source.setPIDSourceType(PIDSourceType.kDisplacement);
		initialPositionLeft = source.pidGetLeft();
		initialPositionRight = source.pidGetRight();
		source.setPIDSourceType(type);
		gyroCorrection.clearInitialValue();
	}
	
	/**
	 * Sets the trajectory for the profiler
	 * @param trajectory
	 */
	public void setTrajectory(TwoDimensionalTrajectory trajectory) {
		this.trajectory = trajectory;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public TwoDimensionalTrajectory getTrajectory() {
		return trajectory;
	}
	
}
