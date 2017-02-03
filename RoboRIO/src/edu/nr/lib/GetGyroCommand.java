package edu.nr.lib;

public class GetGyroCommand extends NRCommand {
	
	AngleGyroCorrection gyro;
	
	protected void onStart() {
		gyro = new AngleGyroCorrection(AngleUnit.DEGREE);
	}

	public AngleGyroCorrection getCorrection() {
		return gyro;
	}
}
