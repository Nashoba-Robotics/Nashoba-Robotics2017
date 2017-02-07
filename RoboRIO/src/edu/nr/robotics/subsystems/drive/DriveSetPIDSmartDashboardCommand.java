package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSetPIDSmartDashboardCommand extends NRCommand {


	@Override
	protected void onStart() {
		SmartDashboard.putNumber("Drive P", SmartDashboard.getNumber("Drive P", Drive.P));
		SmartDashboard.putNumber("Drive I", SmartDashboard.getNumber("Drive I", Drive.I));
		SmartDashboard.putNumber("Drive D", SmartDashboard.getNumber("Drive D", Drive.D));
		SmartDashboard.putNumber("Drive F", SmartDashboard.getNumber("Drive F", Drive.F));
		
		Drive.getInstance().setPID(SmartDashboard.getNumber("Drive P", Drive.P), 
								   SmartDashboard.getNumber("Drive I", Drive.I), 
								   SmartDashboard.getNumber("Drive D", Drive.D), 
								   SmartDashboard.getNumber("Drive F", Drive.F));
	}
    
}

