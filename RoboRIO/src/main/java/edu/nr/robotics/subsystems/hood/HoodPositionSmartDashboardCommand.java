package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HoodPositionSmartDashboardCommand extends NRCommand {

	public void onStart() {
		SmartDashboard.putNumber("Hood Position Setpoint", SmartDashboard.getNumber("Hood Position Setpoint", 0));
		new HoodPositionCommand(new Angle(SmartDashboard.getNumber("Hood Position Setpoint", 0), Angle.Type.DEGREE)).start();
	}

}
