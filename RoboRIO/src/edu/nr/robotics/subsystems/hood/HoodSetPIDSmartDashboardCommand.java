package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HoodSetPIDSmartDashboardCommand extends NRCommand {

	public HoodSetPIDSmartDashboardCommand() {
		requires(Hood.getInstance());
	}
	
	@Override
	protected void onStart() {
		SmartDashboard.putNumber("Hood P", SmartDashboard.getNumber("Hood P", Hood.P));
		SmartDashboard.putNumber("Hood I", SmartDashboard.getNumber("Hood I", Hood.I));
		SmartDashboard.putNumber("Hood D", SmartDashboard.getNumber("Hood D", Hood.D));
		
		Hood.getInstance().setPID(SmartDashboard.getNumber("Hood P", Hood.P), 
								     SmartDashboard.getNumber("Hood I", Hood.I), 
								     SmartDashboard.getNumber("Hood D", Hood.D));
	}
    
}

