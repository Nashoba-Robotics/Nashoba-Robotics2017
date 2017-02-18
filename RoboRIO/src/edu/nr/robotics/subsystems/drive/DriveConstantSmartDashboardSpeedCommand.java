package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Don't call from a CommandGroup
 */
public class DriveConstantSmartDashboardSpeedCommand extends NRCommand {
	
	@Override
	public void onStart() {
		SmartDashboard.putNumber("Drive Left Goal Speed", SmartDashboard.getNumber("Drive Left Goal Speed", 0));
		SmartDashboard.putNumber("Drive Right Goal Speed", SmartDashboard.getNumber("Drive Right Goal Speed", 0));
		double leftSpeed = SmartDashboard.getNumber("Drive Left Goal Speed", 0);
		double rightSpeed = SmartDashboard.getNumber("Drive Right Goal Speed", 0);
		new DriveConstantSpeedCommand(leftSpeed, rightSpeed).start();
	}
	
}
