package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.subsystems.drive.Drive.Gear;

public class DriveForwardBasicCommand extends NRCommand {

	double percent;
	Distance distance;
	Distance encoderDistance;
	
	public DriveForwardBasicCommand(double percent, Distance distance) {
		this.percent = percent;
		this.distance = distance;
	}
	
	@Override
	public void onStart() {
		encoderDistance = Drive.getInstance().getLeftPosition();
		if (Drive.getInstance().getCurrentGear() == Gear.high) {
			Drive.getInstance().setMotorSpeedInPercent(percent, percent);
		} else {
			Drive.getInstance().setMotorSpeedInPercent(percent, percent);
		}
	}
	
	public void onEnd() {
		Drive.getInstance().disable();
	}
	
	@Override
	public boolean isFinishedNR() {
		return (Drive.getInstance().getLeftPosition().sub(encoderDistance)).abs().greaterThan(distance);
	}
}