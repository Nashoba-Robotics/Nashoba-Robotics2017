package edu.nr.robotics;

import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;

public class Calibration {

	public static AngularSpeed getShooterSpeedFromDistance(Distance dist) {
		return AngularSpeed.ZERO; //TODO: Calibration: Map distance to shooter speed
	}

	public static Angle getHoodAngleFromDistance(Distance dist) {
		return Angle.ZERO; //TODO: Calibration: Map distance to hood angle
	}

	public static Time getTimeInAirFromDistance(Distance distance) {
		 //TODO: Turret: Map turret distance to ball time in air and add time it takes for turret to move to spot and ball to shoot		return null;
		return Time.ZERO;
	}

}