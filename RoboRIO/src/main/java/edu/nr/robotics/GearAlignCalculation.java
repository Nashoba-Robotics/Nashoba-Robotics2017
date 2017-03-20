package edu.nr.robotics;

import edu.nr.lib.NRMath;
import edu.nr.lib.Units;
import edu.nr.lib.network.NetworkingDataTypeListener;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Distance.Unit;
import edu.nr.lib.units.Time;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
		//System.out.println("Val: " + value + " type: " + type);
		if(type.identifier == 'a') {
			lastSeenAngle = new Angle(value, (Angle.Unit) type.unit);
		} else if(type.identifier == 'd') {
			lastSeenDistance = new Distance(value, (Distance.Unit) type.unit);
		}
		timeOfLastData = Time.getCurrentTime();
		
		// TODO: Figure out how to get angle of target
		Angle lastSeenTargetAngle = Angle.ZERO;
		
		/*
		Angle theta4 = lastSeenTargetAngle.add(lastSeenAngle);
		Distance distanceToPeg = NRMath.lawOfCos(lastSeenDistance, FieldMap.PEG_LENGTH, theta4);
		Angle angleToPeg = NRMath.asin(FieldMap.PEG_LENGTH.mul(theta4.sin()).div(distanceToPeg)).add(lastSeenAngle);
		*/
		Angle angleToPeg = lastSeenAngle;
		Distance distanceToPeg = lastSeenDistance;
		
		Distance r = NRMath.hypot(RobotMap.GEAR_TO_CENTER_DIST_X, RobotMap.GEAR_TO_CENTER_DIST_Y);
		Angle theta0 = Units.RIGHT_ANGLE.add(NRMath.atan2(RobotMap.GEAR_TO_CENTER_DIST_Y, RobotMap.GEAR_TO_CENTER_DIST_X));
		Angle theta1 = Angle.ZERO;
		if (angleToPeg.greaterThan(NRMath.atan2(RobotMap.GEAR_TO_CENTER_DIST_X, RobotMap.GEAR_TO_CENTER_DIST_Y))) {
			theta1 = Units.FULL_CIRCLE.sub(theta0).sub(angleToPeg);
		} else {
			theta1 = theta0.add(angleToPeg);
		}
		Distance d = NRMath.lawOfCos(distanceToPeg, r, theta1);
		Angle theta2 = NRMath.asin(distanceToPeg.mul(theta1.sin()).div(d));
		Angle theta3 = Units.HALF_CIRCLE.sub(theta0).sub(NRMath.asin(r.mul(theta0.sin()).div(d)));
		if (angleToPeg.greaterThan(NRMath.atan2(RobotMap.GEAR_TO_CENTER_DIST_X, RobotMap.GEAR_TO_CENTER_DIST_Y))) {
			turnAngle = theta2.add(theta3);
		} else {
			turnAngle = theta3.sub(theta2);
		}
		
		driveDistance = d.sub(RobotMap.GEAR_TO_CENTER_DIST_Y).add(FieldMap.DRIVE_DEPTH_ON_PEG_FROM_SHIP);
		SmartDashboard.putNumber("Drive Distance", driveDistance.get(Distance.Unit.INCH));
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
