package edu.nr.robotics;

import edu.nr.lib.NRMath;
import edu.nr.lib.NavX;
import edu.nr.lib.Units;
import edu.nr.lib.network.NetworkingDataTypeListener;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Angle.Unit;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.turret.Turret;

public class AutoTrackingCalculation implements NetworkingDataTypeListener {
		
	Angle turretAngle = Angle.ZERO;
	
	Angle hoodAngle = Angle.ZERO;
	
	AngularSpeed shooterSpeed = AngularSpeed.ZERO;
	
	private Time lastSeenTimeStamp;
	private Angle lastSeenAngle;
	private double lastSeenDistance;
	
	private Time timeOfLastData;
	
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
			lastSeenAngle = new Angle(value, Angle.Unit.DEGREE);
		} else if(type.identifier == 'd') {
			lastSeenDistance = value;
		} else if(type.identifier == 't') {
			lastSeenTimeStamp = new Time(value, Time.Unit.MILLISECOND);
		}
		timeOfLastData = Time.getCurrentTime();
		
		Angle thetaYCamera = NRMath.atan2(RobotMap.Y_CAMERA_OFFSET, RobotMap.X_CAMERA_OFFSET);
		Angle thetaXTurret = NRMath.atan2(RobotMap.X_TURRET_OFFSET, RobotMap.Y_TURRET_OFFSET);
		Angle thetaYTurret = NRMath.atan2(RobotMap.Y_TURRET_OFFSET, RobotMap.X_TURRET_OFFSET);
		
		//Code until break manipulates camera angle as if on center of robot
		double z1 = Math.hypot(RobotMap.X_CAMERA_OFFSET, RobotMap.Y_CAMERA_OFFSET);
		Angle theta4 = Units.HALF_CIRCLE.sub(thetaYCamera).sub(thetaXTurret);
		double h4 = Math.hypot(RobotMap.X_TURRET_OFFSET, RobotMap.Y_TURRET_OFFSET);
		double h3 = NRMath.lawOfCos(h4, z1, theta4);
		Angle theta5 = Turret.getInstance().getHistoricalPosition(lastSeenTimeStamp).sub(thetaYCamera);
		Angle theta6 = Units.RIGHT_ANGLE.sub(theta5).sub(NRMath.asin(h4 * theta4.sin()).mul(1/h3));
		double histDistCenter = NRMath.lawOfCos(lastSeenDistance, h3, theta6.add(lastSeenAngle));
		Angle histAngleCenter = Units.HALF_CIRCLE.sub(thetaXTurret).sub(NRMath.asin(z1 * theta4.sin() / h3)).sub(NRMath.asin(lastSeenDistance * theta6.add(lastSeenAngle).sin() / histDistCenter));
		
		Angle histRobotOrientation = histAngleCenter.add(Turret.getInstance().getHistoricalPosition(lastSeenTimeStamp));
		Angle deltaAngle = NavX.getInstance().getYaw().sub(NavX.getInstance().getHistoricalYaw(lastSeenTimeStamp));
		Angle curRobotOrientation = histRobotOrientation.add(deltaAngle);
		double histLeftPos = Drive.getInstance().getHistoricalLeftPosition(lastSeenTimeStamp).get(Distance.Unit.INCH);
		double histRightPos = Drive.getInstance().getHistoricalRightPosition(lastSeenTimeStamp).get(Distance.Unit.INCH);
		
		//Code until next break to get current distance and turret orientation
		Angle theta1 = histRobotOrientation.add(Units.RIGHT_ANGLE);
		double r = Math.max(histLeftPos, histRightPos) / deltaAngle.get(Unit.RADIAN) - (0.5 * Drive.WHEEL_BASE.get(Distance.Unit.INCH));
		double h = NRMath.lawOfCos(r, histDistCenter, theta1);
		Angle theta0 = NRMath.asin(histDistCenter * theta1.sin() / h).sub(deltaAngle);
		double curDist = NRMath.lawOfCos(h, r, theta0);
		double hyp = Math.hypot(RobotMap.X_TURRET_OFFSET, RobotMap.Y_TURRET_OFFSET);
		Angle theta3 = new Angle(0.5, Angle.Unit.ROTATION).sub(thetaXTurret).add(curRobotOrientation);
		double curDistReal = NRMath.lawOfCos(curDist, hyp, theta3);
		Angle curTurretOrientation = Units.RIGHT_ANGLE.sub(NRMath.asin(curDist * theta3.sin() / curDistReal)).sub(thetaYTurret);
		
		//Gets average speed of two drive sides to get instantaneous speed in (inches / sec)
		double speed = NRMath.average(Drive.getInstance().getLeftSpeed(),Drive.getInstance().getRightSpeed()).get(Distance.Unit.INCH, Time.Unit.SECOND);
		double vertSpeed = speed * curRobotOrientation.cos();
		
		//Code until next break gets additional angle for turret to turn based on current speed
		double timeUntilMake = 0; //TODO: Turret: Map turret distance to ball time in air and add time it takes for turret to move to spot and ball to shoot
		double p = speed * (timeUntilMake);
		double e = NRMath.lawOfCos(curDistReal, p, curTurretOrientation);
		turretAngle = new Angle(0.5, Angle.Unit.ROTATION).sub(NRMath.asin(curDistReal * curTurretOrientation.sin() / e));
		//Sets the change in position of the turret
		
		//What the distance of the shot will map as due to forward/backward motion
		@SuppressWarnings("unused")
		double feltDist = vertSpeed * timeUntilMake;
		//TODO: Hood: Map feltDist to hood angle
		hoodAngle = Angle.ZERO;
		
		//TODO: Shooter: Map feltDist to shooter speed
		shooterSpeed = AngularSpeed.ZERO;
	}
	
	/**
	 * @return Turret angle in degrees
	 */
	public Angle getTurretAngle() {
		return turretAngle;
	}

	/**
	 * @return Hood angle in degrees
	 */
	public Angle getHoodAngle() {
		return hoodAngle;
	}
	
	/**
	 * @return Shooter speed in rpm
	 */
	public AngularSpeed getShooterSpeed() {
		return shooterSpeed;
	}
	
	public boolean canSeeTarget() {
		return Time.getCurrentTime().sub(timeOfLastData).lessThan(FieldMap.MIN_TRACKING_WAIT_TIME);
	}
}
