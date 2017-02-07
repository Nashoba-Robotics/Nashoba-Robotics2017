package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;

public class DriveConstantSpeedCommand extends NRCommand {
	
	double leftSpeed, rightSpeed;
	
	public DriveConstantSpeedCommand(double leftSpeed, double rightSpeed) {
		super(Drive.getInstance());
		
		this.leftSpeed = leftSpeed;
		this.rightSpeed = rightSpeed;
		
	}
	
	@Override
	public void onExecute() {
		Drive.getInstance().tankDrive(leftSpeed, rightSpeed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
}
