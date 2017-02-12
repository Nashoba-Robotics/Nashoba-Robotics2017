package edu.nr.lib.motionprofiling;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import edu.nr.lib.AngleGyroCorrection;
import edu.nr.lib.AngleUnit;
import edu.nr.lib.NavX;
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
import jaci.pathfinder.modifiers.TankModifier;

public class TwoDimensionalMotionProfilerPathfinder extends TimerTask  {

	private final Timer timer;
	
	//In milliseconds
	private final long period;
	private static final long defaultPeriod = 20; //50 Hz 
		
	private boolean enabled = false;
	private DoublePIDOutput out;
	private DoublePIDSource source;
	
	private double ka, kp, ki, kd, kv, kp_theta;
	
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

	double timeSinceStart = 0;
	double lastTime = 0;
		
	public TwoDimensionalMotionProfilerPathfinder(DoublePIDOutput out, DoublePIDSource source, double kv, double ka, double kp, double ki, double kd, double kp_theta, double max_velocity, double max_acceleration, double max_jerk, int encoderTicksPerRevolution, double wheelDiameter, long period) {
		this.out = out;
		this.source = source;
		this.period = period;
		this.trajectoryConfig = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, this.period/1000.0, max_velocity, max_acceleration, max_jerk);
        this.points = new Waypoint[] {
				new Waypoint(0,0,0),
				new Waypoint(1,0,0)
        };
		this.trajectory = Pathfinder.generate(points, trajectoryConfig);
		this.modifier = new TankModifier(trajectory).modify(0.67948718);
		this.left = new DistanceFollower(modifier.getLeftTrajectory());
		this.right = new DistanceFollower(modifier.getRightTrajectory());
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 0, this.period);
		this.source.setPIDSourceType(PIDSourceType.kDisplacement);
		this.ka = ka;
		this.kp = kp;
		this.ki = ki;
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
		
		//new Thread(this).start();
	}
	
	public TwoDimensionalMotionProfilerPathfinder(DoublePIDOutput out, DoublePIDSource source, double kv, double ka, double kp, double ki, double kd, double kp_theta, double max_velocity, double max_acceleration, double max_jerk, int encoderTicksPerRevolution, double wheelDiameter) {
		this(out, source, kv, ka, kp, ki, kd, kp_theta, max_velocity, max_acceleration, max_jerk, encoderTicksPerRevolution, wheelDiameter, defaultPeriod);
	}
	
	double timeOfVChange = 0;
	double prevV;
	
	@Override
	public void run() {
			//System.out.println("Running!");
		
			if(enabled) {
				lastTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();

				System.out.println("Enabled!");
				//double prelimOutputLeft = 0;
				//double prelimOutputRight = 0;
				
				//source.pidGetLeft();
				//source.pidGetRight();
				
				double prelimOutputLeft = left.calculate((source.pidGetLeft() - initialPositionLeft)/(1 / (RobotMap.WHEEL_DIAMETER * Math.PI * .0254)) /*Rotations per meter*/);
				double prelimOutputRight = -right.calculate(-(source.pidGetRight() - initialPositionRight) / (1 / (RobotMap.WHEEL_DIAMETER * Math.PI * .0254)) /*Rotations per meter*/);
				
				double currentHeading = -NavX.getInstance().getYaw(AngleUnit.DEGREE);
				//double currentHeading = -gyroCorrection.getAngleErrorDegrees();
				double desiredHeading = Pathfinder.r2d(left.getHeading());
				
				double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - currentHeading);
				
				double headingAdjustment = -kp_theta * angleDifference;
				//double headingAdjustment = 0;
				
				double outputLeft = prelimOutputLeft + headingAdjustment;
				double outputRight = prelimOutputRight + headingAdjustment;
				
				out.pidWrite(outputLeft, outputRight);
				
				SmartDashboard.putNumber("Output Left", outputLeft);
				SmartDashboard.putNumber("Output Right", outputRight);
				
				int place = (int)((edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - timeSinceStart) * 1000 / this.period);
				
				int spot = Math.min(place, modifier.getLeftTrajectory().length() - 1);
				
				if(spot > 0) {
					SmartDashboard.putString("Motion Profiler Angle", Pathfinder.boundHalfDegrees(currentHeading)+ " : " + Pathfinder.boundHalfDegrees(desiredHeading) + " : " + Pathfinder.boundHalfDegrees(Pathfinder.r2d(modifier.getLeftTrajectory().get(spot).heading)));
					SmartDashboard.putString("Motion Profiler X Left String",(source.pidGetLeft() - initialPositionLeft) / ((1 / (RobotMap.WHEEL_DIAMETER * Math.PI * .0254)) /*Rotations per meter*/) + " : " + modifier.getLeftTrajectory().get(spot).position);
					SmartDashboard.putString("Motion Profiler X Right String", -(source.pidGetRight() - initialPositionRight) / (1 / (RobotMap.WHEEL_DIAMETER * Math.PI * .0254)) /*Rotations per meter*/ + " : " + modifier.getRightTrajectory().get(spot).position);
				}
				
				
				/*
				if(place < modifier.getLeftTrajectory().length()) {
					SmartDashboard.putString("Motion Profiler X Left String",(source.pidGetLeft() - initialPositionLeft) / 3.581 + " : " + modifier.getLeftTrajectory().get(place).x);
					SmartDashboard.putString("Motion Profiler X Right String", -(source.pidGetRight() - initialPositionRight) / 3.581 + " : " + modifier.getRightTrajectory().get(place).position);
					//SmartDashboard.putString("Motion Profiler Right Position Testing", source.pidGetRight() + ":" + initialPositionRight);
					//SmartDashboard.putString("Motion Profiler Right Position Testing Part 2", (source.pidGetRight()) / 3.581 + ":" + (initialPositionRight) / 3.581);
				}
				*/
				
				//source.setPIDSourceType(PIDSourceType.kRate);
				//SmartDashboard.putString("Motion Profiler V Left", source.pidGetLeft() + ":" + -(outputLeft / (RobotMap.WHEEL_DIAMETER * Math.PI * 0.0254)));
				//SmartDashboard.putString("Motion Profiler V Right", source.pidGetRight()  + ":" + (outputRight / (RobotMap.WHEEL_DIAMETER * Math.PI * 0.0254)));
				//source.setPIDSourceType(PIDSourceType.kDisplacement);
				
				double deltaT = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - lastTime;
				
				System.out.println("Time since last update: " + deltaT);
	
				SmartDashboard.putNumber("Delta T", deltaT);

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
		left.configurePIDVA(kp, ki, kd, kv, ka);
		right.configurePIDVA(kp, ki, kd, kv, ka);
		source.setPIDSourceType(type);
		gyroCorrection.clearInitialValue();
		timeSinceStart = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
		lastTime = timeSinceStart;
		NavX.getInstance().reset();
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
		
		for(int i = 0; i < modifier.getLeftTrajectory().segments.length; i += 25) {
			DecimalFormat df = new DecimalFormat("#.#");
			df.setMinimumFractionDigits(1);
			df.setMinimumIntegerDigits(3);
			
			//System.out.println("left:\t" + i*period + "ms:\t" + df.format(39.37*modifier.getLeftTrajectory().get(i).x)    + ", \t" + df.format(39.37*modifier.getLeftTrajectory().get(i).y));
			//System.out.println("right:\t" + i*period + "ms:\t" + df.format(39.37*modifier.getRightTrajectory().get(i).x)    + ", \t" + df.format(39.37*modifier.getRightTrajectory().get(i).y));
		}	
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
	
	public void setKI(double ki) {
		this.ki = ki;
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
