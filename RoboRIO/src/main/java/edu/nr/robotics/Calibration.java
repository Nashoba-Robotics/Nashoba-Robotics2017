package edu.nr.robotics;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;

public class Calibration {

	static final Distance hopperShotLimit = new Distance(120, Distance.Unit.INCH);
	
	public static AngularSpeed getShooterSpeedFromDistance(Distance dist) {
		if(dist.lessThan(hopperShotLimit)) {
			return new AngularSpeed(2.7*dist.get(Distance.Unit.INCH) + 2026,Angle.Unit.ROTATION, Time.Unit.MINUTE);
		} else {
			return new AngularSpeed(7.22*dist.get(Distance.Unit.INCH) + 1396,Angle.Unit.ROTATION, Time.Unit.MINUTE);
		}
		//TODO: Calibration: Map distance to shooter speed
	}

	public static Angle getHoodAngleFromDistance(Distance dist) {
		if(dist.lessThan(hopperShotLimit)) {
			return new Angle(0.0162*dist.get(Distance.Unit.INCH) + 16.97, Angle.Unit.DEGREE);
		} else {
			return new Angle(0.0278*dist.get(Distance.Unit.INCH) + 19.24, Angle.Unit.DEGREE);
		}
		//return new Angle(dist.get(Distance.Unit.FOOT), Angle.Unit.DEGREE); //TODO: Calibration: Map distance to hood angle
	}

	public static Time getTimeInAirFromDistance(Distance distance) {
		 //TODO: Turret: Map turret distance to ball time in air and add time it takes for turret to move to spot and ball to shoot		return null;
		return Time.ZERO;
	}

}
