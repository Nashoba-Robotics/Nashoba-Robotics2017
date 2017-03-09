package edu.nr.robotics;

import edu.nr.lib.NRMath;
import edu.nr.lib.Units;
import edu.nr.lib.network.NetworkingDataTypeListener;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;

public class GearAlignCalculation implements NetworkingDataTypeListener {
	
	Angle turnAngle = Angle.ZERO;
	Distance driveDistance = Distance.ZERO;
	
	private Angle lastSeenAngle = Angle.ZERO;
	private Distance lastSeenDistance = Distance.ZERO;
	
	private Time timeOfLastData = Time.ZERO;
	
	private static GearAlignCalculation singleton;
	
	public synchronized static void init() {
		if (singleton == null)
			singleton = new GearAlignCalculation();
	}
	
	public static GearAlignCalculation getInstance() {
		if (singleton == null) {
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
		}
		timeOfLastData = Time.getCurrentTime();
		
		System.out.println("Dist: " + lastSeenDistance + " angle: " + lastSeenAngle);
	
		Distance tempDistOne = NRMath.lawOfCos(lastSeenDistance, RobotMap.GEAR_TO_CENTER_DIST_X, Units.RIGHT_ANGLE.sub(lastSeenAngle));
		Angle tempAngleOne = NRMath.lawOfCos(lastSeenDistance, RobotMap.GEAR_TO_CENTER_DIST_X, tempDistOne);
		turnAngle = Units.RIGHT_ANGLE.sub(tempAngleOne);
		//driveDistance = NRMath.hypot(lastSeenDistance.mul(lastSeenAngle.cos()).add(RobotMap.GEAR_CAMERA_TO_CENTER_OF_ROBOT_DIST_Y), lastSeenDistance.mul(lastSeenAngle.sin())).sub(FieldMap.DRIVE_DEPTH_ON_PEG_FROM_SHIP);
		//turnAngle = NRMath.atan2(lastSeenDistance.mul(lastSeenAngle.sin()),lastSeenDistance.mul(lastSeenAngle.cos()).add(RobotMap.GEAR_CAMERA_TO_CENTER_OF_ROBOT_DIST_Y));
	}
	
	public Distance getDistToDrive() {
		return driveDistance;
	}
	
	public Angle getAngleToTurn() {
		return turnAngle;
	}
	
	public boolean canSeeTarget() {
		return Time.getCurrentTime().sub(timeOfLastData).lessThan(FieldMap.MIN_TRACKING_WAIT_TIME);
	}
}
