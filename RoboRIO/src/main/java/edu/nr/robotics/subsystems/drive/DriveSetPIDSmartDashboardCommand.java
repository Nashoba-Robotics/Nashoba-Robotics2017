package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.subsystems.drive.Drive.Gear;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSetPIDSmartDashboardCommand extends NRCommand {

	Gear gear;
	
	public DriveSetPIDSmartDashboardCommand(Gear gear) {
		this.gear = gear;
	}
	
	
	@Override
	protected void onStart() {
		if(gear == Gear.high) {
			SmartDashboard.putNumber("Drive High P", SmartDashboard.getNumber("Drive High P", Drive.P_HIGH_GEAR));
			SmartDashboard.putNumber("Drive High I", SmartDashboard.getNumber("Drive High I", Drive.I_HIGH_GEAR));
			SmartDashboard.putNumber("Drive High D", SmartDashboard.getNumber("Drive High D", Drive.D_HIGH_GEAR));
			SmartDashboard.putNumber("Drive High F", SmartDashboard.getNumber("Drive High F", Drive.F_HIGH_GEAR));
			
			Drive.getInstance().setPID(SmartDashboard.getNumber("Drive High P", Drive.P_HIGH_GEAR), 
									   SmartDashboard.getNumber("Drive High I", Drive.I_HIGH_GEAR), 
									   SmartDashboard.getNumber("Drive High D", Drive.D_HIGH_GEAR), 
									   SmartDashboard.getNumber("Drive High F", Drive.F_HIGH_GEAR), gear);
		} else {
			SmartDashboard.putNumber("Drive Low P", SmartDashboard.getNumber("Drive Low P", Drive.P_LOW_GEAR));
			SmartDashboard.putNumber("Drive Low I", SmartDashboard.getNumber("Drive Low I", Drive.I_LOW_GEAR));
			SmartDashboard.putNumber("Drive Low D", SmartDashboard.getNumber("Drive Low D", Drive.D_LOW_GEAR));
			SmartDashboard.putNumber("Drive Low F", SmartDashboard.getNumber("Drive Low F", Drive.F_LOW_GEAR));
			
			Drive.getInstance().setPID(SmartDashboard.getNumber("Drive Low P", Drive.P_LOW_GEAR), 
									   SmartDashboard.getNumber("Drive Low I", Drive.I_LOW_GEAR), 
									   SmartDashboard.getNumber("Drive Low D", Drive.D_LOW_GEAR), 
									   SmartDashboard.getNumber("Drive Low F", Drive.F_LOW_GEAR), gear);
		}
	}
    
}

