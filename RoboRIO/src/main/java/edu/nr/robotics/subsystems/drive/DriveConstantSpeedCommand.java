package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;

public class DriveConstantSpeedCommand extends NRCommand {
	
	double leftSpeed, rightSpeed;
	
	/**
	 * From -1 to 1
	 * @param leftSpeed
	 * @param rightSpeed
	 */
	public DriveConstantSpeedCommand(double leftSpeed, double rightSpeed) {
		super(Drive.getInstance());
		
		this.leftSpeed = leftSpeed;
		this.rightSpeed = rightSpeed;
		
	}
	
	@Override
	public void onExecute() {
		Drive.getInstance().setMotorSpeedInPercent(leftSpeed, rightSpeed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
}
