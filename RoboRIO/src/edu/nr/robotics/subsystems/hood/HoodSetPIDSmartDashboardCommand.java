package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HoodSetPIDSmartDashboardCommand extends NRCommand {

	public HoodSetPIDSmartDashboardCommand() {
		requires(Hood.getInstance());
	}

	@Override
	protected void onStart() {
		if (Hood.getInstance().isMotionMagicMode()) {
			SmartDashboard.putNumber("Hood P Motion Magic",
					SmartDashboard.getNumber("Hood P Motion Magic", Hood.P_MOTION_MAGIC));
			SmartDashboard.putNumber("Hood I Motion Magic",
					SmartDashboard.getNumber("Hood I Motion Magic", Hood.I_MOTION_MAGIC));
			SmartDashboard.putNumber("Hood D Motion Magic",
					SmartDashboard.getNumber("Hood D Motion Magic", Hood.D_MOTION_MAGIC));
			Hood.getInstance().setPID(SmartDashboard.getNumber("Hood P Motion Magic", Hood.P_MOTION_MAGIC),
					SmartDashboard.getNumber("Hood I Motion Magic", Hood.I_MOTION_MAGIC),
					SmartDashboard.getNumber("Hood D Motion Magic", Hood.D_MOTION_MAGIC));
		} else {
			SmartDashboard.putNumber("Hood P Operator Control",
					SmartDashboard.getNumber("Hood P Operator Control", Hood.P_OPERATOR_CONTROL));
			SmartDashboard.putNumber("Hood I Operator Control",
					SmartDashboard.getNumber("Hood I Operator Control", Hood.I_OPERATOR_CONTROL));
			SmartDashboard.putNumber("Hood D Operator Control",
					SmartDashboard.getNumber("Hood D Operator Control", Hood.D_OPERATOR_CONTROL));
			Hood.getInstance().setPID(SmartDashboard.getNumber("Hood P Operator Control", Hood.P_OPERATOR_CONTROL),
					SmartDashboard.getNumber("Hood I Operator Control", Hood.I_OPERATOR_CONTROL),
					SmartDashboard.getNumber("Hood D Operator Control", Hood.D_OPERATOR_CONTROL));
		}
	}
}
