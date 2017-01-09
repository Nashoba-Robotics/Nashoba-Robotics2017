package edu.nr.lib.motionprofiling;

import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OneDimensionalMotionProfilerBasic extends TimerTask implements OneDimensionalMotionProfiler  {

	private final Timer timer;
	
	//In milliseconds
	private final long period;
	private static final long defaultPeriod = 5; //200 Hz 
	
	private double prevTime;
	private double startTime;
	
	private boolean enabled = false;
	private PIDOutput out;
	private PIDSource source;
	
	private double ka, kp, kd, kv;
	private double errorLast;
	
	private double initialPosition;
			
	private OneDimensionalTrajectory trajectory;
		
	public OneDimensionalMotionProfilerBasic(PIDOutput out, PIDSource source, double kv, double ka, double kp, double kd, long period) {
		this.out = out;
		this.source = source;
		this.period = period;
		this.trajectory = new OneDimensionalTrajectorySimple(0,1,1,1);
		timer = new Timer();
		timer.schedule(this, 0, this.period);
		reset();
		this.source.setPIDSourceType(PIDSourceType.kDisplacement);
		this.ka = ka;
		this.kp = kp;
		this.kd = kd;
		this.kv = kv;
		this.initialPosition = source.pidGet();
	}
	
	public OneDimensionalMotionProfilerBasic(PIDOutput out, PIDSource source, double kv, double ka, double kp, double kd) {
		this(out, source, kv, ka, kp, kd, defaultPeriod);
	}
	
	double timeOfVChange = 0;
	double prevV;
	
	@Override
	public void run() {
		if(enabled) {
			double dt = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - prevTime;
						
			double velocityGoal = trajectory.getGoalVelocity(edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime);
			
			double accelGoal = trajectory.getGoalAccel(edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime);
			
			double error = trajectory.getGoalPosition(edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime) - source.pidGet() + initialPosition;
						
			double errorDeriv = (error - errorLast) / dt;
			
			double output = velocityGoal * kv + accelGoal * ka + error * kp + errorDeriv * kd;
			
			out.pidWrite(output);

			errorLast = error;
			
			double timeSinceStart = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime;
			
			if((initialPosition + (trajectory.getGoalPosition(timeSinceStart))) < 0) {
				//double goalPosition = trajectory.getGoalPosition(timeSinceStart);
				//double currentTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
				
				isEnabled();
			}

			source.setPIDSourceType(PIDSourceType.kRate);
			SmartDashboard.putString("Motion Profiler V", source.pidGet() + ":" + (output * trajectory.getMaxPossibleVelocity() * Math.signum(trajectory.getMaxPossibleVelocity())));
			source.setPIDSourceType(PIDSourceType.kDisplacement);
			SmartDashboard.putString("Motion Profiler X", source.pidGet() + ":" 
					+ (initialPosition + (trajectory.getGoalPosition(edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime))));
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
		errorLast = 0;
		startTime = prevTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
		PIDSourceType type = source.getPIDSourceType();
		source.setPIDSourceType(PIDSourceType.kDisplacement);
		initialPosition = source.pidGet();
		source.setPIDSourceType(type);
	}
	
	/**
	 * Sets the trajectory for the profiler
	 * @param trajectory
	 */
	public void setTrajectory(OneDimensionalTrajectory trajectory) {
		this.trajectory = trajectory;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public OneDimensionalTrajectory getTrajectory() {
		return trajectory;
	}
	
}
