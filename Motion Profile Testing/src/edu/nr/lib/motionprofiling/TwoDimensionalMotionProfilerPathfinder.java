package edu.nr.lib.motionprofiling;

import java.util.Timer;
import java.util.TimerTask;

import edu.nr.lib.AngleGyroCorrection;
import edu.nr.lib.interfaces.DoublePIDOutput;
import edu.nr.lib.interfaces.DoublePIDSource;
import edu.nr.lib.interfaces.GyroCorrection;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.DistanceFollower;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class TwoDimensionalMotionProfilerPathfinder extends TimerTask  {

	private final Timer timer;
	
	//In milliseconds
	private final long period;
	private static final long defaultPeriod = 10; //100 Hz 
		
	private boolean enabled = false;
	private DoublePIDOutput out;
	private DoublePIDSource source;
	
	private double ka, kp, kd, kv, kp_theta;
	
	private double initialPositionLeft;
	private double initialPositionRight;
			
	private Trajectory trajectory;
	private Trajectory.Config trajectoryConfig;
	private TankModifier modifier;
	
	GyroCorrection gyroCorrection;
	
	DistanceFollower left;
	DistanceFollower right;
	
	Waypoint[] points;
	
	int encoderTicksPerRevolution;
	double wheelDiameter;

		
	public TwoDimensionalMotionProfilerPathfinder(DoublePIDOutput out, DoublePIDSource source, double kv, double ka, double kp, double kd, double kp_theta, double max_velocity, double max_acceleration, double max_jerk, int encoderTicksPerRevolution, double wheelDiameter, long period) {
		this.out = out;
		this.source = source;
		this.period = period;
		this.trajectoryConfig = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, this.period/1000.0, max_velocity, max_acceleration, max_jerk);
        this.points = new Waypoint[] {
				new Waypoint(0,0,0),
				new Waypoint(-4,-1,0)

        };
		this.trajectory = Pathfinder.generate(points, trajectoryConfig);
		this.modifier = new TankModifier(trajectory).modify(0.67948718);
		this.left = new DistanceFollower(modifier.getLeftTrajectory());
		this.right = new DistanceFollower(modifier.getRightTrajectory());
		timer = new Timer();
		timer.schedule(this, 0, this.period);
		this.source.setPIDSourceType(PIDSourceType.kDisplacement);
		this.ka = ka;
		this.kp = kp;
		this.kd = kd;
		this.kv = kv;
		this.kp_theta = kp_theta;
		this.encoderTicksPerRevolution = encoderTicksPerRevolution;
		this.initialPositionLeft = source.pidGetLeft();
		this.initialPositionRight = source.pidGetRight();
		this.gyroCorrection = new AngleGyroCorrection();
		gyroCorrection.clearInitialValue();
		this.wheelDiameter = wheelDiameter;
		reset();
	}
	
	public TwoDimensionalMotionProfilerPathfinder(DoublePIDOutput out, DoublePIDSource source, double kv, double ka, double kp, double kd, double kp_theta, double max_velocity, double max_acceleration, double max_jerk, int encoderTicksPerRevolution, double wheelDiameter) {
		this(out, source, kv, ka, kp, kd, kp_theta, max_velocity, max_acceleration, max_jerk, encoderTicksPerRevolution, wheelDiameter, defaultPeriod);
	}
	
	double timeOfVChange = 0;
	double prevV;
	
	@Override
	public void run() {
		//System.out.println("Running!");
				
		if(enabled) {
			System.out.println("Enabled!");
			double prelimOutputLeft = left.calculate((source.pidGetLeft() - initialPositionLeft)/3.579); //Actually should be the number of rotations per meter
			double prelimOutputRight = -right.calculate(-(source.pidGetRight() - initialPositionRight)/3.579); //Actually should be the number of rotations per meter
			
			double currentHeading = gyroCorrection.getAngleErrorDegrees();
			double desiredHeading = Pathfinder.r2d(left.getHeading());
			
			SmartDashboard.putString("Motion Profiler Angle", currentHeading+ " : " +desiredHeading);
			
			double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - currentHeading);
			
			double headingAdjustment = kp_theta * angleDifference;
			
			double outputLeft = prelimOutputLeft + headingAdjustment;
			double outputRight = prelimOutputRight + headingAdjustment;
			
			out.pidWrite(outputLeft, outputRight);
			
			
			
			SmartDashboard.putNumber("Output Left", outputLeft);
			SmartDashboard.putNumber("Output Right", outputRight);
			
			source.setPIDSourceType(PIDSourceType.kRate);
			SmartDashboard.putString("Motion Profiler V Left", source.pidGetLeft() + ":" + -(outputLeft / (RobotMap.WHEEL_DIAMETER * Math.PI * 0.0254)));
			SmartDashboard.putString("Motion Profiler V Right", source.pidGetRight()  + ":" + (outputRight / (RobotMap.WHEEL_DIAMETER * Math.PI * 0.0254)));
			source.setPIDSourceType(PIDSourceType.kDisplacement);
			SmartDashboard.putNumber("Motion Profiler X Left", source.pidGetLeft()/256 * RobotMap.WHEEL_DIAMETER * Math.PI * 0.0254);
			SmartDashboard.putNumber("Motion Profiler X Right", source.pidGetRight()/256 * RobotMap.WHEEL_DIAMETER * Math.PI * 0.0254);
		}
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
		PIDSourceType type = source.getPIDSourceType();
		source.setPIDSourceType(PIDSourceType.kDisplacement);
		initialPositionLeft = source.pidGetLeft();
		initialPositionRight = source.pidGetRight();
		left.reset();
		right.reset();
		left.configurePIDVA(kp, 0, kd, kv, ka);
		right.configurePIDVA(kp, 0, kd, kv, ka);
		source.setPIDSourceType(type);
		gyroCorrection.clearInitialValue();
	}
	
	/**
	 * Sets the trajectory for the profiler
	 * @param trajectory
	 */
	public void setTrajectory(Waypoint[] points) {
		this.points = points;
		this.trajectory = Pathfinder.generate(points, trajectoryConfig);
		this.modifier = new TankModifier(trajectory).modify(0.67948718);
		this.left = new DistanceFollower(modifier.getLeftTrajectory());
		this.right = new DistanceFollower(modifier.getRightTrajectory());
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public void setKA(double ka) {
		this.ka = ka;
	}
	
	public void setKP(double kp) {
		this.kp = kp;
	}
	
	public void setKD(double kd) {
		this.kd = kd;
	}

	public void setKV(double kv) {
		this.kv = kv;
	}

	public void setKP_theta(double kp_theta) {
		this.kp_theta = kp_theta;
	}
	
}
