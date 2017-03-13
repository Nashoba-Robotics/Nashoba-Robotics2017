package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.GyroCorrection;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;

public class DriveForwardBasicCommand extends NRCommand {

	double percent;
	Distance distance;
	Distance encoderDistance;
	GyroCorrection gyro;
	
	public DriveForwardBasicCommand(double percent, Distance distance) {
		super(Drive.getInstance());
		this.percent = percent;
		this.distance = distance;
		gyro = new GyroCorrection();
	}
	
	@Override
	public void onStart() {
		encoderDistance = Drive.getInstance().getLeftPosition();
		gyro.reset();

	}
	
	@Override
	public void onExecute() {
		double turnValue = gyro.getTurnValue();
		//double turnValue = GearAlignCalculation.getInstance().getAngleToTurn().get(Angle.Unit.DEGREE) * GyroCorrection.DEFAULT_KP_THETA;
		Drive.getInstance().setMotorSpeedInPercent(percent - turnValue, percent + turnValue);
	}
	
	public void onEnd() {
		Drive.getInstance().disable();
	}
	
	@Override
	public boolean isFinishedNR() {
		return (Drive.getInstance().getLeftPosition().sub(encoderDistance)).abs().greaterThan(distance);
	}
}
