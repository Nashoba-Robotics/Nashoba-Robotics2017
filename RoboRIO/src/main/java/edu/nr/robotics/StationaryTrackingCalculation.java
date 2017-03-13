package edu.nr.robotics;

import edu.nr.lib.NRMath;
import edu.nr.lib.Units;
import edu.nr.lib.network.NetworkingDataTypeListener;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;

public class StationaryTrackingCalculation implements NetworkingDataTypeListener {
		
	Angle turretAngle = Angle.ZERO;
	
	Angle hoodAngle = Angle.ZERO;
	
	AngularSpeed shooterSpeed = AngularSpeed.ZERO;
	
	private Time lastSeenTimeStamp;
	private Angle lastSeenAngle;
	private Distance lastSeenDistance;
	
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
			lastSeenAngle = new Angle(value, (Angle.Unit) type.unit);
		} else if(type.identifier == 'd') {
			lastSeenDistance = new Distance(value, (Distance.Unit) type.unit);
		} else if(type.identifier == 't') {
			lastSeenTimeStamp = new Time(value, (Time.Unit) type.unit);
		}
		timeOfLastData = Time.getCurrentTime();
		
		Distance distReal = NRMath.lawOfCos(lastSeenDistance, RobotMap.Y_CAMERA_OFFSET, lastSeenAngle);
		
		Angle theta4 = NRMath.asin(distReal.mul(lastSeenAngle.sin()).div(lastSeenDistance));
		
		if (theta4.greaterThan(Angle.ZERO)) {
			turretAngle = Units.HALF_CIRCLE.sub(theta4);
		} else if (theta4.equals(Angle.ZERO)) {
			turretAngle = Angle.ZERO;
		} else {
			turretAngle = Units.HALF_CIRCLE.negate().sub(theta4);
		}

		hoodAngle = Calibration.getHoodAngleFromDistance(distReal);

		shooterSpeed = Calibration.getShooterSpeedFromDistance(distReal);

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
