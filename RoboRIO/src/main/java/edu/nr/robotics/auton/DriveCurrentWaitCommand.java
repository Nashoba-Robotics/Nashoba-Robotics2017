package edu.nr.robotics.auton;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.subsystems.drive.Drive;

public class DriveCurrentWaitCommand extends NRCommand {

	double threshold;
	
	public DriveCurrentWaitCommand(double threshold) {
		this.threshold = threshold;
	}

	@Override
	public boolean isFinishedNR() {
		return Drive.getInstance().getLeftCurrent() > threshold || Drive.getInstance().getRightCurrent() > threshold;
	}
}
