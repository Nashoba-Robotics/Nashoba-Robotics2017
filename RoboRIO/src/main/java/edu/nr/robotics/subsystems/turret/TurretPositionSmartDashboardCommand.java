package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurretPositionSmartDashboardCommand extends NRCommand {

	public void onStart() {
		SmartDashboard.putNumber("Turret Position Setpoint", SmartDashboard.getNumber("Turret Position Setpoint", 0));
		new TurretPositionCommand(SmartDashboard.getNumber("Turret Position Setpoint", 0)).start();
	}
	
}
