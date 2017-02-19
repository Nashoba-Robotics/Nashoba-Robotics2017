package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HoodDeltaPositionSmartDashboardCommand extends NRCommand {
	
	@Override
	public void onStart() {
		SmartDashboard.putNumber("Hood Delta Position Setpoint", SmartDashboard.getNumber("Hood Delta Position Setpoint", 0));
		new HoodDeltaPositionCommand(new Angle(SmartDashboard.getNumber("Hood Delta Position Setpoint", 0), Angle.Unit.DEGREE)).start();
	}
	
}
