package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;

public class DriveTurnConstantSpeedCommand extends NRCommand {
	
	double turnSpeed;
	
	public DriveTurnConstantSpeedCommand(double turnSpeed) {
		super(Drive.getInstance());
		
		this.turnSpeed = turnSpeed;
		
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
