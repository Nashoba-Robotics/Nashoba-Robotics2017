package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveConstantSmartDashboardSpeedCommand extends NRCommand {
	
	double leftSpeed, rightSpeed;
	
	String leftSpeedString, rightSpeedString;
	
	double defaultLeftSpeed, defaultRightSpeed;
	
	public DriveConstantSmartDashboardSpeedCommand(String leftSpeedString, String rightSpeedString, double defaultLeftSpeed, double defaultRightSpeed) {
		super(Drive.getInstance());		
		
		this.leftSpeedString = leftSpeedString;
		this.rightSpeedString = rightSpeedString;
		
		this.defaultLeftSpeed = defaultLeftSpeed;
		this.defaultRightSpeed = defaultRightSpeed;
	}
	
	@Override
	public void onStart() {
		leftSpeed = SmartDashboard.getNumber(leftSpeedString, defaultLeftSpeed);
		rightSpeed = SmartDashboard.getNumber(rightSpeedString, defaultRightSpeed);
	}
	
	@Override
	public void onExecute() {
		Drive.getInstance().tankDrive(leftSpeed, rightSpeed);
		leftSpeed = SmartDashboard.getNumber(leftSpeedString, leftSpeed);
		rightSpeed = SmartDashboard.getNumber(rightSpeedString, rightSpeed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
}
