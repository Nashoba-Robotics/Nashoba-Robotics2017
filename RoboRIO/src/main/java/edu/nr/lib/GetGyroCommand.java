package edu.nr.lib;

import edu.nr.lib.commandbased.NRCommand;

public class GetGyroCommand extends NRCommand {
	
	GyroCorrection gyro;
	
	protected void onStart() {
		gyro = new GyroCorrection(AngleUnit.DEGREE);
	}

	public GyroCorrection getCorrection() {
		return gyro;
	}
}
