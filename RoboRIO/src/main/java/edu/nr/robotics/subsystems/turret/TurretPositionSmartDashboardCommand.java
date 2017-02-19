package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurretPositionSmartDashboardCommand extends NRCommand {

	public void onStart() {
		SmartDashboard.putNumber("Turret Position Setpoint", SmartDashboard.getNumber("Turret Position Setpoint", 0));
		new TurretPositionCommand(new Angle(SmartDashboard.getNumber("Turret Position Setpoint", 0), Unit.DEGREE)).start();
	}
	
}
