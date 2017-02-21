package edu.nr.lib;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

/**
 *
 */
public class WaitUntilGyroCommand extends NRCommand {

	GyroCorrection gyroCorrection;
	Angle angle;
	GetGyroCommand gyro;

	public WaitUntilGyroCommand(Angle angle) {
		this.angle = angle;
	}

	public WaitUntilGyroCommand(Angle angle, GetGyroCommand gyro) {
		this.angle = angle;
		this.gyro = gyro;
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinishedNR() {
		System.out.println("Error: " + (gyroCorrection.getAngleError().sub(angle)));
		double degrees = angle.get(Angle.Unit.DEGREE);
		if (degrees > 0)
			return gyroCorrection.getAngleError().get(Angle.Unit.DEGREE) > degrees;
		else
			return gyroCorrection.getAngleError().get(Angle.Unit.DEGREE) < degrees;
	}

	@Override
	protected void onStart() {
        if(gyro != null)
        	gyroCorrection = gyro.getCorrection();
        else
            gyroCorrection = new GyroCorrection();
	}
	
}
