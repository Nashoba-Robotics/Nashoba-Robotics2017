package edu.nr.lib;

import edu.nr.lib.commandbased.NRCommand;

public class GetGyroCommand extends NRCommand {
	
	NavXGyroCorrection gyro;
	
	protected void onStart() {
		gyro = new NavXGyroCorrection(AngleUnit.DEGREE);
	}

	public NavXGyroCorrection getCorrection() {
		return gyro;
	}
}
