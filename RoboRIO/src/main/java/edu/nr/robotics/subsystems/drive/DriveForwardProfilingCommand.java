package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.units.Distance;

public class DriveForwardProfilingCommand extends DriveForwardProfilingExtendableCommand {

	Distance distance;

	/**
	 * Drive forward
	 * 
	 * @param distance
	 */
	public DriveForwardProfilingCommand(Distance distance, double speed) {
		super(speed);
		this.distance = distance;
	}
	
	/**
	 * Drive forward
	 * 
	 * @param distance
	 */
	public DriveForwardProfilingCommand(Distance distance) {
		this.distance = distance;
	}
	
	@Override
	public Distance distanceToGo() {
		return distance;
	}
}
