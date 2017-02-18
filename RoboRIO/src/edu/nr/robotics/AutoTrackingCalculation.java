package edu.nr.robotics;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NavX;
import edu.nr.lib.Units;
import edu.nr.lib.network.NetworkingDataTypeListener;
import edu.nr.lib.network.TCPServer;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.turret.Turret;

public class AutoTrackingCalculation implements NetworkingDataTypeListener {
		
	double turretAngle = 0;
	
	/**
	 * Degrees
	 */
	double hoodAngle = 0;
	
	/**
	 * RPM
	 */
	double shooterSpeed = 0;
	
	private long lastSeenTimeStamp;
	private double lastSeenAngle;
	private double lastSeenDistance;
	
	private long timeOfLastData;
	
	/**
	 * The amount of time to wait without a picture before sweeping
	 * 
	 * TODO: General: Determine the max wait time before sweeping turret
	 */
	public static final long MIN_TRACKING_WAIT_TIME = 0;
	
	private static AutoTrackingCalculation singleton;
	
	public synchronized static void init() {
		if(singleton == null) {
			singleton = new AutoTrackingCalculation();
		}
	}
	
	public static AutoTrackingCalculation getInstance() {
		if(singleton == null) {
			init();
		}
		return singleton;
	}
	
	@Override
	public void updateDataType(TCPServer.NetworkingDataType type, double value) {
		if(type.identifier == 'a') {
			lastSeenAngle = value;
		} else if(type.identifier == 'd') {
			lastSeenDistance = value;
		} else if(type.identifier == 't') {
			lastSeenTimeStamp = (long) value;
		}
		timeOfLastData = getCurrentTimeMillis();
		
		//Code until break manipulates camera angle as if on center of robot
		double z1 = Math.sqrt(Math.pow(RobotMap.X_CAMERA_OFFSET, 2) + Math.pow(RobotMap.Y_CAMERA_OFFSET, 2));
		double theta4 = 180 - Math.atan(RobotMap.Y_CAMERA_OFFSET / RobotMap.X_CAMERA_OFFSET) - Math.atan(RobotMap.X_TURRET_OFFSET / RobotMap.Y_TURRET_OFFSET);
		double h4 = Math.sqrt(Math.pow(RobotMap.X_TURRET_OFFSET, 2) + Math.pow(RobotMap.Y_TURRET_OFFSET, 2));
		double h3 = Math.sqrt(Math.pow(h4, 2) + Math.pow(z1, 2) - 2 * h4 * z1 * Math.cos(theta4));
		double theta5 = Turret.getInstance().getHistoricalPosition(lastSeenTimeStamp) * Units.DEGREES_PER_ROTATION - Math.atan(RobotMap.Y_CAMERA_OFFSET / RobotMap.X_CAMERA_OFFSET);
		double theta6 = 90 - theta5 - Math.asin(h4 * Math.sin(theta4) / h3);
		double histDistCenter = Math.sqrt(Math.pow(lastSeenDistance, 2) + Math.pow(h3, 2) - 2 * lastSeenDistance * h3 * Math.cos(theta6 + lastSeenAngle));
		double histAngleCenter = 180 - Math.atan(RobotMap.X_TURRET_OFFSET / RobotMap.Y_TURRET_OFFSET) - z1 * Math.sin(theta4) / h3 - lastSeenDistance * Math.sin(theta6 + lastSeenAngle) / histDistCenter;
		
		double histRobotOrientation = histAngleCenter + Turret.getInstance().getHistoricalPosition(lastSeenTimeStamp) * Units.DEGREES_PER_ROTATION;
		double deltaAngle = (NavX.getInstance().getYaw(AngleUnit.DEGREE) - NavX.getInstance().getHistoricalYaw(AngleUnit.DEGREE, lastSeenTimeStamp));
		double curRobotOrientation = histRobotOrientation + deltaAngle;
		double histLeftPos = Drive.getInstance().getHistoricalLeftPosition(lastSeenTimeStamp) * Units.DEGREES_PER_ROTATION;
		double histRightPos = Drive.getInstance().getHistoricalRightPosition(lastSeenTimeStamp) * Units.DEGREES_PER_ROTATION;
		
		//Code until next break to get current distance and turret orientation
		double theta1 = histRobotOrientation + 90;
		double r = Math.max(histLeftPos, histRightPos) / deltaAngle - (0.5 * Drive.WHEEL_BASE);
		double h = Math.sqrt(Math.pow(r, 2) + Math.pow(histDistCenter, 2) - 2 * r * histDistCenter * Math.cos(theta1));
		double theta0 = Math.asin(histDistCenter * Math.sin(theta1) / h) - deltaAngle;
		double curDist = Math.sqrt(Math.pow(h, 2) + Math.pow(r, 2) - 2 * h * r * Math.cos(theta0));
		double hyp = Math.sqrt(Math.pow(RobotMap.X_TURRET_OFFSET, 2) + Math.pow(RobotMap.Y_TURRET_OFFSET, 2));
		double theta3 = 180 - Math.atan(RobotMap.X_TURRET_OFFSET / RobotMap.Y_TURRET_OFFSET) + curRobotOrientation;
		double curDistReal = Math.sqrt(Math.pow(curDist, 2) + Math.pow(hyp, 2) - 2 * curDist * hyp * Math.cos(theta3));
		double curTurretOrientation = 90 - Math.asin(curDist * Math.sin(theta3) / curDistReal) - Math.atan(RobotMap.Y_TURRET_OFFSET / RobotMap.X_TURRET_OFFSET);
		
		//Gets average speed of two drive sides to get instantaneous speed in (inches / sec)
		double speed = (Drive.getInstance().getLeftSpeed() + Drive.getInstance().getRightSpeed()) / 2 * (Drive.WHEEL_DIAMETER * Units.INCHES_PER_FOOT) / Units.SECONDS_PER_MINUTE;
		double vertSpeed = speed * Math.cos(curRobotOrientation);
		
		//Code until next break gets additional angle for turret to turn based on current speed
		double timeUntilMake = 0; //TODO: Turret: Map turret distance to ball time in air and add time it takes for turret to move to spot and ball to shoot
		double p = speed * (timeUntilMake);
		double e = Math.sqrt(Math.pow(curDistReal, 2) + Math.pow(p, 2) - 2 * curDistReal * p * Math.cos(curTurretOrientation));
		turretAngle = 180 - Math.asin(curDistReal * Math.sin(curTurretOrientation) / e);
		//Sets the change in position of the turret
		turretAngle /= Units.DEGREES_PER_ROTATION; // Changes from angle to rotations
		
		//What the distance of the shot will map as due to forward/backward motion
		double feltDist = vertSpeed * timeUntilMake;
		//TODO: Hood: Map feltDist to hood angle in degrees
		hoodAngle = 0;
		
		//TODO: Shooter: Map feltDist to shooter speed in rpm
		shooterSpeed = 0;
	}
	
	/**
	 * @return Turret angle in degrees
	 */
	public double getTurretAngle() {
		return turretAngle;
	}

	/**
	 * @return Hood angle in degrees
	 */
	public double getHoodAngle() {
		return hoodAngle;
	}
	
	/**
	 * @return Shooter speed in rpm
	 */
	public double getShooterSpeed() {
		return shooterSpeed;
	}
	
	private long getCurrentTimeMillis() {
		return (long) (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() * Units.MILLISECONDS_PER_SECOND);
	}
	
	public boolean canSeeTarget() {
		return (getCurrentTimeMillis() - timeOfLastData) < AutoTrackingCalculation.MIN_TRACKING_WAIT_TIME;
	}
}
