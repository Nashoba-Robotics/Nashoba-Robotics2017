package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.GyroCorrection;
import edu.nr.lib.commandbased.NRCommand;

public class DriveForwardForeverBasicCommand extends NRCommand {

	double percent;
	GyroCorrection gyro;
	
	public DriveForwardForeverBasicCommand(double percent) {
		super(Drive.getInstance());
		this.percent = percent;
		gyro = new GyroCorrection();
	}
	
	@Override
	public void onStart() {
		gyro.reset();

	}
	
	@Override
	public void onExecute() {
		//double turnValue = gyro.getTurnValue();
		//double turnValue = GearAlignCalculation.getInstance().getAngleToTurn().get(Angle.Unit.DEGREE) * 0.02;
		double turnValue = 0;
		Drive.getInstance().setMotorSpeedInPercent(percent - turnValue, percent + turnValue);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
