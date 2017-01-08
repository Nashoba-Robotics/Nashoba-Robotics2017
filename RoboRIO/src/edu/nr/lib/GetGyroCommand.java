package edu.nr.lib;

public class GetGyroCommand extends NRCommand {
	
	AngleGyroCorrectionSource gyro;
	
	protected void onStart() {
		gyro = new AngleGyroCorrectionSource(AngleUnit.DEGREE);
	}

	public AngleGyroCorrectionSource getCorrection() {
		return gyro;
	}
}
