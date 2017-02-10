package edu.nr.lib;

import edu.nr.lib.commandbased.NRCommand;

public class GetGyroCommand extends NRCommand {
	
	AngleGyroCorrection gyro;
	
	protected void onStart() {
		gyro = new AngleGyroCorrection(AngleUnit.DEGREE);
	}

	public AngleGyroCorrection getCorrection() {
		return gyro;
	}
}
