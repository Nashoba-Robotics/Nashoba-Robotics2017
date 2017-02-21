package edu.nr.robotics;

import edu.nr.lib.NRMath;
import edu.nr.lib.network.NetworkingDataTypeListener;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;

public class GearAlignCalculation implements NetworkingDataTypeListener {

	/**
	 * The distance parallel to drive direction that the camera is from the center of rotation of the robot.
	 * TODO: Gear: Get this number
	 */
	public static final Distance CAMERA_TO_CENTER_OF_ROBOT_DIST_Y = Distance.ZERO;
	
	/**
	 * The distance to stop away from the tape of the gear
	 * TODO: Gear: Get this other number
	 */
	public static final Distance DISTANCE_TO_STOP_FROM_GEAR = Distance.ZERO;
	
	Angle turnAngle = Angle.ZERO;
	Distance driveDistance = Distance.ZERO;
	
	private Angle lastSeenAngle;
	private Distance lastSeenDistance;
	
	private Time timeOfLastData;
	
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
	
		driveDistance = NRMath.hypot(lastSeenDistance.mul(lastSeenAngle.cos()).add(CAMERA_TO_CENTER_OF_ROBOT_DIST_Y), lastSeenDistance.mul(lastSeenAngle.sin())).sub(DISTANCE_TO_STOP_FROM_GEAR);
		turnAngle = NRMath.atan2(lastSeenDistance.mul(lastSeenAngle.sin()),lastSeenDistance.mul(lastSeenAngle.cos()).add(CAMERA_TO_CENTER_OF_ROBOT_DIST_Y));
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
