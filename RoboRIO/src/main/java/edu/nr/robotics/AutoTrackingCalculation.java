package edu.nr.robotics;

import edu.nr.lib.NRMath;
import edu.nr.lib.NavX;
import edu.nr.lib.Units;
import edu.nr.lib.network.NetworkingDataTypeListener;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Speed;
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
	private Distance lastSeenDistance;
	
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
			lastSeenAngle = new Angle(value, (Angle.Unit) type.unit);
		} else if(type.identifier == 'd') {
			lastSeenDistance = new Distance(value, (Distance.Unit) type.unit);
		} else if(type.identifier == 't') {
			lastSeenTimeStamp = new Time(value, (Time.Unit) type.unit);
		}
		timeOfLastData = Time.getCurrentTime();
				
		Angle thetaYCamera = NRMath.atan2(RobotMap.Y_CAMERA_OFFSET, RobotMap.X_CAMERA_OFFSET);
		Angle thetaXTurret = NRMath.atan2(RobotMap.X_TURRET_OFFSET, RobotMap.Y_TURRET_OFFSET);
		Angle thetaYTurret = NRMath.atan2(RobotMap.Y_TURRET_OFFSET, RobotMap.X_TURRET_OFFSET);
		
		//Code until break manipulates camera angle as if on center of robot
		Distance z1 = NRMath.hypot(RobotMap.X_CAMERA_OFFSET, RobotMap.Y_CAMERA_OFFSET);
		Angle theta4 = Units.HALF_CIRCLE.sub(thetaYCamera).sub(thetaXTurret);
		Distance h4 = NRMath.hypot(RobotMap.X_TURRET_OFFSET, RobotMap.Y_TURRET_OFFSET);
		Distance h3 = NRMath.lawOfCos(h4, z1, theta4);
		Angle theta5 = Turret.getInstance().getHistoricalPosition(lastSeenTimeStamp).sub(thetaYCamera);
		Angle theta6 = Units.RIGHT_ANGLE.sub(theta5).sub(NRMath.asin(h4.mul(theta4.sin()).div(h3)));
		Distance histDistCenter = NRMath.lawOfCos(lastSeenDistance, h3, theta6.add(lastSeenAngle));
		Angle histAngleCenter = Units.HALF_CIRCLE.sub(thetaXTurret).sub(NRMath.asin(z1.mul(theta4.sin()).div(h3))).sub(NRMath.asin(lastSeenDistance.mul(theta6.add(lastSeenAngle).sin()).div(histDistCenter)));
		
		Angle histRobotOrientation = histAngleCenter.add(Turret.getInstance().getHistoricalPosition(lastSeenTimeStamp));
		Angle deltaAngle = NavX.getInstance().getYaw().sub(NavX.getInstance().getHistoricalYaw(lastSeenTimeStamp));
		Angle curRobotOrientation = histRobotOrientation.add(deltaAngle);
		Distance histLeftPos = Drive.getInstance().getHistoricalLeftPosition(lastSeenTimeStamp);
		Distance histRightPos = Drive.getInstance().getHistoricalRightPosition(lastSeenTimeStamp);
		
		//Code until next break to get current distance and turret orientation
		Angle theta1 = histRobotOrientation.add(Units.RIGHT_ANGLE);
		Distance r = NRMath.max(histLeftPos, histRightPos).mul(1.0/deltaAngle.get(Unit.RADIAN)).sub(Drive.WHEEL_BASE.mul(0.5));
		Distance h = NRMath.lawOfCos(r, histDistCenter, theta1);
		Angle theta0 = NRMath.asin(histDistCenter.mul(theta1.sin()).div(h)).sub(deltaAngle);
		Distance curDist = NRMath.lawOfCos(h, r, theta0);
		Distance hyp = NRMath.hypot(RobotMap.X_TURRET_OFFSET, RobotMap.Y_TURRET_OFFSET);
		Angle theta3 = new Angle(0.5, Angle.Unit.ROTATION).sub(thetaXTurret).add(curRobotOrientation);
		Distance curDistReal = NRMath.lawOfCos(curDist, hyp, theta3);
		Angle curTurretOrientation = Units.RIGHT_ANGLE.sub(NRMath.asin(curDist.mul(theta3.sin()).div(curDistReal))).sub(thetaYTurret);
		
		//Gets average speed of two drive sides to get instantaneous speed in (inches / sec)
		Speed speed = NRMath.average(Drive.getInstance().getLeftSpeed(),Drive.getInstance().getRightSpeed());
		
		//Code until next break gets angle for turret to be at based on current speed
		Time timeUntilMake = Calibration.getTimeInAirFromDistance(curDistReal);
		Distance p = speed.mul(timeUntilMake);
		//What the distance of the shot will map as due to forward/backward motion
		Distance feltDist = NRMath.lawOfCos(curDistReal, p, curTurretOrientation);
		turretAngle = Units.HALF_CIRCLE.sub(NRMath.asin(curDistReal.mul(curTurretOrientation.sin()).div(feltDist)));
		//Sets the change in position of the turret
		
		hoodAngle = Calibration.getHoodAngleFromDistance(feltDist);
		
		shooterSpeed = Calibration.getShooterSpeedFromDistance(feltDist);
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
