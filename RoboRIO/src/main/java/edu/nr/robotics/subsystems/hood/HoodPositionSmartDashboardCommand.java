package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HoodPositionSmartDashboardCommand extends NRCommand {

	public void onStart() {
		SmartDashboard.putNumber("Hood Position Setpoint", SmartDashboard.getNumber("Hood Position Setpoint", 0));
		new HoodPositionCommand(SmartDashboard.getNumber("Hood Position Setpoint", 0)).start();
	}

}
