package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTurnConstantSmartDashboardSpeedCommand extends NRCommand {
	
	double turnSpeed;
	
	String turnSpeedString;
	
	double defaultTurnSpeed;
	
	public DriveTurnConstantSmartDashboardSpeedCommand(String turnSpeedString, double defaultTurnSpeed) {
		super(Drive.getInstance());		
		
		this.turnSpeedString = turnSpeedString;
		
		this.defaultTurnSpeed = defaultTurnSpeed;
	}
	
	@Override
	public void onStart() {
		turnSpeed = SmartDashboard.getNumber(turnSpeedString, defaultTurnSpeed);
	}
	
	@Override
	public void onExecute() {
		Drive.getInstance().arcadeDrive(0, turnSpeed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
}
