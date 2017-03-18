package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.GyroCorrection;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;

public class DriveStationaryPIDCommand extends NRCommand {

	double p, pTheta;
	
	public DriveStationaryPIDCommand(double p, double pTheta) {
		super(Drive.getInstance());
		this.p = p;
		this.pTheta = pTheta;
		gyro = new GyroCorrection();

	}
	
	Distance initialPositionLeft;
	Distance initialPositionRight;
	
	GyroCorrection gyro;
	
	@Override
	public void onStart() {
		initialPositionLeft = Drive.getInstance().getLeftPosition();
		initialPositionRight = Drive.getInstance().getRightPosition();
		
		gyro.reset();
	}
	
	@Override
	public void onExecute() {
		Distance currentPositionLeft = Drive.getInstance().getLeftPosition();
		Distance currentPositionRight = Drive.getInstance().getRightPosition();
		
		Distance deltaPositionLeft = currentPositionLeft.sub(initialPositionLeft);
		Distance deltaPositionRight = currentPositionRight.sub(initialPositionRight);
		
		double turnValue = gyro.getTurnValue(pTheta);
		double leftMoveValue = -deltaPositionLeft.get(Distance.Unit.METER) * p;
		double rightMoveValue = -deltaPositionRight.get(Distance.Unit.METER) * p;
				
		Drive.getInstance().tankDrive(leftMoveValue + turnValue, rightMoveValue - turnValue);
	}
	
}
