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
	
	private Time lastSeenTimeStamp = Time.ZERO;
	private Angle lastSeenAngle = Angle.ZERO;
	private Distance lastSeenDistance = Distance.ZERO;
	
	private Time timeOfLastData = Time.ZERO;
	
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
		
		Distance hypot = NRMath.hypot(RobotMap.X_TURRET_OFFSET, RobotMap.Y_TURRET_OFFSET);
		
		Distance histDistCenter = Distance.ZERO;
		Angle histAngleCenter = Angle.ZERO;
		
		Distance turretHistDistReal = NRMath.lawOfCos(lastSeenDistance, RobotMap.Y_CAMERA_OFFSET, lastSeenAngle);
		
		Angle histTurretPosition = Turret.getInstance().getHistoricalPosition(lastSeenTimeStamp);
		
		Angle tempTheta1;
		Angle tempTheta2;
		Angle tempTheta3;
		Angle tempTheta4;
		Distance tempDist1;
		if (lastSeenAngle.greaterThan(Angle.ZERO) && histTurretPosition.greaterThan(Angle.ZERO)) {
			//Distance turretHistDistReal = NRMath.lawOfCos(lastSeenDistance, RobotMap.Y_CAMERA_OFFSET, lastSeenAngle);
			tempTheta1 = Units.FULL_CIRCLE.sub(thetaYTurret).sub(Units.RIGHT_ANGLE.sub(histTurretPosition)).sub(NRMath.lawOfCos(RobotMap.Y_CAMERA_OFFSET, turretHistDistReal, lastSeenDistance));
			histDistCenter = NRMath.lawOfCos(hypot, turretHistDistReal, tempTheta1);
			histAngleCenter = Units.HALF_CIRCLE.sub(thetaXTurret).sub(NRMath.lawOfCos(histDistCenter, hypot, turretHistDistReal));
		} else if (lastSeenAngle.lessThan(Angle.ZERO) && histTurretPosition.greaterThan(Angle.ZERO)) {
			tempTheta1 = NRMath.asin(lastSeenDistance.mul(histTurretPosition.sin()).div(turretHistDistReal)).abs().sub(Units.RIGHT_ANGLE.sub(histTurretPosition));
			if (tempTheta1.greaterThan(thetaYTurret)) {
				tempTheta2 = tempTheta1.sub(thetaYTurret);
				histDistCenter = NRMath.lawOfCos(hypot, turretHistDistReal, tempTheta2);
				histAngleCenter = Units.HALF_CIRCLE.sub(thetaXTurret).sub(NRMath.lawOfCos(histDistCenter, hypot, turretHistDistReal));
			} else if (tempTheta1.lessThan(thetaYTurret)) {
				tempTheta2 = NRMath.lawOfCos(turretHistDistReal, RobotMap.Y_TURRET_OFFSET, lastSeenDistance).sub(Units.RIGHT_ANGLE.sub(histTurretPosition));
				tempTheta3 = thetaYTurret.sub(tempTheta2);
				histDistCenter = NRMath.lawOfCos(turretHistDistReal, hypot, tempTheta3);
				histAngleCenter = Units.HALF_CIRCLE.sub(NRMath.asin(turretHistDistReal.mul(tempTheta3.sin()).div(histDistCenter)).sub(thetaXTurret)).negate();
			}
		} else if (lastSeenAngle.greaterThan(Angle.ZERO) && histTurretPosition.lessThan(Angle.ZERO)) {
			if (Units.HALF_CIRCLE.sub(NRMath.asin(lastSeenDistance.mul(lastSeenAngle.sin()).div(turretHistDistReal)).sub(Units.RIGHT_ANGLE.sub(histTurretPosition))).greaterThan(thetaYTurret)) {
				tempTheta1 = Units.FULL_CIRCLE.sub(Units.RIGHT_ANGLE).sub(thetaYTurret).sub(histTurretPosition.abs());
				tempDist1 = NRMath.lawOfCos(hypot, RobotMap.Y_CAMERA_OFFSET, tempTheta1);
				tempTheta2 = NRMath.asin(hypot.mul(tempTheta1.sin()).div(tempDist1));
				tempTheta3 = lastSeenAngle.sub(tempTheta2).sub(Units.RIGHT_ANGLE.sub(histTurretPosition));
				histDistCenter = NRMath.lawOfCos(tempDist1, lastSeenDistance, tempTheta3);
				histAngleCenter = Units.HALF_CIRCLE.sub(thetaXTurret).sub(NRMath.lawOfCos(hypot, tempDist1, RobotMap.Y_CAMERA_OFFSET));
			} else if (Units.HALF_CIRCLE.sub(NRMath.asin(lastSeenDistance.mul(lastSeenAngle.sin()).div(turretHistDistReal)).sub(Units.RIGHT_ANGLE.sub(histTurretPosition))).lessThan(thetaYTurret)) {
				tempTheta1 = thetaYTurret.sub(Units.HALF_CIRCLE.sub(NRMath.asin(lastSeenDistance.mul(lastSeenAngle.sin()).div(turretHistDistReal)).sub(Units.RIGHT_ANGLE.sub(histTurretPosition))));
				histDistCenter = NRMath.lawOfCos(turretHistDistReal, hypot, tempTheta1);
				tempTheta2 = Units.FULL_CIRCLE.sub(Units.RIGHT_ANGLE).sub(histTurretPosition).sub(thetaYTurret);
				tempDist1 = NRMath.lawOfCos(hypot, RobotMap.Y_CAMERA_OFFSET, tempTheta2);
				tempTheta3 = NRMath.asin(RobotMap.Y_CAMERA_OFFSET.mul(tempTheta2.sin()).div(tempDist1));
				tempTheta4 = NRMath.lawOfCos(histDistCenter, tempDist1, lastSeenDistance);
				histAngleCenter = Units.HALF_CIRCLE.sub(tempTheta4.sub(thetaXTurret).sub(tempTheta3));
			}
		} else if (lastSeenAngle.lessThan(Angle.ZERO) && histTurretPosition.lessThan(Angle.ZERO)) {
			
		}
		
		//Code until break manipulates camera angle as if on center of robot
		Angle theta4 = Units.HALF_CIRCLE.sub(thetaYCamera).sub(thetaXTurret);
		Distance h4 = NRMath.hypot(RobotMap.X_TURRET_OFFSET, RobotMap.Y_TURRET_OFFSET);
		Distance h3 = NRMath.lawOfCos(h4, RobotMap.Y_CAMERA_OFFSET, theta4);
		Angle theta5 = histTurretPosition.sub(thetaYCamera);
		Angle theta6 = Units.RIGHT_ANGLE.sub(theta5).sub(NRMath.asin(h4.mul(theta4.sin()).div(h3)));
		histDistCenter = NRMath.lawOfCos(lastSeenDistance, h3, theta6.add(lastSeenAngle));
		histAngleCenter = Units.HALF_CIRCLE.sub(thetaXTurret).sub(NRMath.asin(RobotMap.Y_CAMERA_OFFSET.mul(theta4.sin()).div(h3))).sub(NRMath.asin(lastSeenDistance.mul(theta6.add(lastSeenAngle).sin()).div(histDistCenter)));
		
		Angle histRobotOrientation = histAngleCenter.add(histTurretPosition);
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
		Angle theta3 = new Angle(0.5, Angle.Unit.ROTATION).sub(thetaXTurret).add(curRobotOrientation);
		Distance curDistReal = NRMath.lawOfCos(curDist, hypot, theta3);
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

	public Angle getRawTurretAngle() {
		return lastSeenAngle;
	}
}
