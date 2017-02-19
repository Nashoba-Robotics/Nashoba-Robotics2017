package edu.nr.robotics;

import edu.nr.lib.Units;
import edu.nr.lib.network.NetworkingDataTypeListener;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Type;

public class GearAlignCalculation implements NetworkingDataTypeListener {

	/**
	 * The distance parallel to drive direction that the camera is from the center of rotation of the robot.
	 * In inches
	 */
	public static final double CAMERA_TO_CENTER_OF_ROBOT_DIST_Y = 0;
	
	/**
	 * The distance to stop away from the tape of the gear
	 */
	public static final double DISTANCE_TO_STOP_FROM_GEAR = 0;
	
	Angle turnAngle = Angle.ZERO;
	double driveDistance = 0;
	
	private double lastSeenAngle;
	private double lastSeenDistance;
	
	private long timeOfLastData;
	
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
			lastSeenAngle = value;
		} else if(type.identifier == 'd') {
			lastSeenDistance = value;
		}
		timeOfLastData = getCurrentTimeMillis();
	
		driveDistance = Math.hypot(lastSeenDistance * Math.cos(lastSeenAngle) + CAMERA_TO_CENTER_OF_ROBOT_DIST_Y, lastSeenDistance * Math.sin(lastSeenAngle)) - DISTANCE_TO_STOP_FROM_GEAR;
		turnAngle = new Angle(Math.atan(lastSeenDistance * Math.sin(lastSeenAngle) / (lastSeenDistance * Math.cos(lastSeenAngle) + CAMERA_TO_CENTER_OF_ROBOT_DIST_Y)), Type.RADIAN);
	}
	
	public double getDistToDrive() {
		return driveDistance;
	}
	
	public Angle getAngleToTurn() {
		return turnAngle;
	}
	
	private long getCurrentTimeMillis() {
		return (long) (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() * Units.MILLISECONDS_PER_SECOND);
	}
	
	public boolean canSeeTarget() {
		return (getCurrentTimeMillis() - timeOfLastData) < AutoTrackingCalculation.MIN_TRACKING_WAIT_TIME;
	}
}
