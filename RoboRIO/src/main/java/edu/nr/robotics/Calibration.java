package edu.nr.robotics;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;

public class Calibration {

	static final Distance hopperShotLimit = new Distance(120, Distance.Unit.INCH);
	
	public static AngularSpeed getShooterSpeedFromDistance(Distance dist) {
		if(dist.lessThan(hopperShotLimit)) {
			return new AngularSpeed(2.4263*dist.get(Distance.Unit.INCH) + 2132,Angle.Unit.ROTATION, Time.Unit.MINUTE);
		} else {
			return new AngularSpeed(10*dist.get(Distance.Unit.INCH) + 950,Angle.Unit.ROTATION, Time.Unit.MINUTE);
		}
		//TODO: Calibration: Map distance to shooter speed
	}

	public static Angle getHoodAngleFromDistance(Distance dist) {
		if(dist.lessThan(hopperShotLimit)) {
			return new Angle(0.0282*dist.get(Distance.Unit.INCH) + 12.147, Angle.Unit.DEGREE);
		} else {
			return new Angle(0.0051*dist.get(Distance.Unit.INCH) + 22.944, Angle.Unit.DEGREE);
		}
		//return new Angle(dist.get(Distance.Unit.FOOT), Angle.Unit.DEGREE); //TODO: Calibration: Map distance to hood angle
	}

	public static Time getTimeInAirFromDistance(Distance distance) {
		 //TODO: Turret: Map turret distance to ball time in air and add time it takes for turret to move to spot and ball to shoot		return null;
		return Time.ZERO;
	}

}
