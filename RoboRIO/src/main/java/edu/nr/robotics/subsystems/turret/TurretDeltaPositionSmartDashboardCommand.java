package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurretDeltaPositionSmartDashboardCommand extends NRCommand {

	public void onStart() {
		SmartDashboard.putNumber("Turret Delta Position Setpoint", SmartDashboard.getNumber("Turret Delta Position Setpoint", 0));
		new TurretDeltaPositionCommand(SmartDashboard.getNumber("Turret Delta Position Setpoint", 0)).start();
	}
	
}
