package edu.nr.lib;

import edu.nr.lib.commandbased.NRCommand;

/**
 *
 */
public class WaitUntilGyroCommand extends NRCommand {

	AngleGyroCorrection gyroCorrection;
	double angle;
	GetGyroCommand gyro;

	public WaitUntilGyroCommand(double angle) {
		this.angle = angle;
	}

	public WaitUntilGyroCommand(double angle, GetGyroCommand gyro) {
		this.angle = angle;
		this.gyro = gyro;
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinishedNR() {
		System.out.println("Error: " + (gyroCorrection.get() - angle));
		if (angle > 0)
			return gyroCorrection.get() > angle;
		else
			return gyroCorrection.get() < angle;
	}

	@Override
	protected void onStart() {
        if(gyro != null)
        	gyroCorrection = gyro.getCorrection();
        else
            gyroCorrection = new AngleGyroCorrection();
	}
	
}
