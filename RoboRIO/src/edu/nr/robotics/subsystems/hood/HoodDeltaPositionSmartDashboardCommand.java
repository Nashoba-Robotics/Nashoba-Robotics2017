package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HoodDeltaPositionSmartDashboardCommand extends NRCommand {
	
	@Override
	public void onStart() {
		SmartDashboard.putNumber("Hood Delta Position Setpoint", SmartDashboard.getNumber("Hood Delta Position Setpoint", 0));
		new HoodDeltaPositionCommand(SmartDashboard.getNumber("Hood Delta Position Setpoint", 0)).start();
	}
	
}
