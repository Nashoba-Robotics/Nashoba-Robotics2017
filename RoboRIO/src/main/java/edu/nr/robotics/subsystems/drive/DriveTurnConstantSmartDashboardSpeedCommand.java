package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTurnConstantSmartDashboardSpeedCommand extends NRCommand {
	
	double turnSpeed;
	
	public DriveTurnConstantSmartDashboardSpeedCommand() {
		super(Drive.getInstance());	
	}
	
	@Override
	public void onStart() {
		SmartDashboard.putNumber("Drive Constant Turn Speed", SmartDashboard.getNumber("Drive Constant Turn Speed", 0));
	}
	
	@Override
	public void onExecute() {
		turnSpeed = SmartDashboard.getNumber("Drive Constant Turn Speed", turnSpeed);
		Drive.getInstance().arcadeDrive(0, turnSpeed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
}
