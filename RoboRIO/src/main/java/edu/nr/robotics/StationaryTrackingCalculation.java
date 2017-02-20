package edu.nr.robotics;

import edu.nr.lib.NRMath;
import edu.nr.lib.Units;
import edu.nr.lib.network.NetworkingDataTypeListener;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.subsystems.turret.Turret;

public class StationaryTrackingCalculation implements NetworkingDataTypeListener {
		
	Angle turretAngle = Angle.ZERO;
	
	Angle hoodAngle = Angle.ZERO;
	
	AngularSpeed shooterSpeed = AngularSpeed.ZERO;
	
	private Time lastSeenTimeStamp;
	private Angle lastSeenAngle;
	private double lastSeenDistance;
	
	private Time timeOfLastData;
	
	private static StationaryTrackingCalculation singleton;
	
	public synchronized static void init() {
		if(singleton == null) {
			singleton = new StationaryTrackingCalculation();
		}
	}
	
	public static StationaryTrackingCalculation getInstance() {
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
		
		//manipulates camera angle as if on center of robot
		double z1 = Math.hypot(RobotMap.X_CAMERA_OFFSET, RobotMap.Y_CAMERA_OFFSET);
		Angle theta4 = Units.HALF_CIRCLE.sub(thetaYCamera).sub(thetaXTurret);
		double h4 = Math.hypot(RobotMap.X_TURRET_OFFSET, RobotMap.Y_TURRET_OFFSET);
		double h3 = NRMath.lawOfCos(h4, z1, theta4);
		Angle theta5 = Turret.getInstance().getHistoricalPosition(lastSeenTimeStamp).sub(thetaYCamera);
		Angle theta6 = Units.RIGHT_ANGLE.sub(theta5).sub(NRMath.asin(h4 * theta4.sin()).mul(1/h3));
		double distCenter = NRMath.lawOfCos(lastSeenDistance, h3, theta6.add(lastSeenAngle));
		Angle angleCenter = Units.HALF_CIRCLE.sub(NRMath.atan2(RobotMap.X_TURRET_OFFSET, RobotMap.Y_TURRET_OFFSET)).sub(NRMath.asin(z1 * theta4.sin() / h3)).sub(NRMath.asin(lastSeenDistance * theta6.add(lastSeenAngle).sin() / distCenter));

		
		//Manipulates camera as if on center of turret
		Angle theta1 = Units.HALF_CIRCLE.sub(angleCenter).sub(thetaXTurret);
		double distReal = NRMath.lawOfCos(distCenter, h4, theta1);
		Angle theta2 = NRMath.asin(distCenter * theta1.sin() / distReal);
		turretAngle = theta2.sub(Units.RIGHT_ANGLE).add(thetaYTurret);

		//TODO: Hood: Map distance of turret to angle of hood
		hoodAngle = Angle.ZERO;

		//TODO: Shooter: Map distance of turret to speed of shooter
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
