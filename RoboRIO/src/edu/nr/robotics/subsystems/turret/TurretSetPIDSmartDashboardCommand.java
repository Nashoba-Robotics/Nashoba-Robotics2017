package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurretSetPIDSmartDashboardCommand extends NRCommand {

	public TurretSetPIDSmartDashboardCommand() {
		requires(Turret.getInstance());
	}
	
	@Override
	protected void onStart() {
		if (Turret.getInstance().isMotionMagicMode()) {
			SmartDashboard.putNumber("Turret P Motion Magic", SmartDashboard.getNumber("Turret P Motion Magic", Turret.P_MOTION_MAGIC));
			SmartDashboard.putNumber("Turret I Motion Magic", SmartDashboard.getNumber("Turret I Motion Magic", Turret.I_MOTION_MAGIC));
			SmartDashboard.putNumber("Turret D Motion Magic", SmartDashboard.getNumber("Turret D Motion Magic", Turret.D_MOTION_MAGIC));
			
			Turret.getInstance().setPID(SmartDashboard.getNumber("Turret P Motion Magic", Turret.P_MOTION_MAGIC), 
								     SmartDashboard.getNumber("Turret I Motion Magic", Turret.I_MOTION_MAGIC), 
								     SmartDashboard.getNumber("Turret D Motion Magic", Turret.D_MOTION_MAGIC));	
		} else {
			SmartDashboard.putNumber("Turret P Operator Control", SmartDashboard.getNumber("Turret P Operator Control", Turret.P_MOTION_MAGIC));
			SmartDashboard.putNumber("Turret I Operator Control", SmartDashboard.getNumber("Turret I Operator Control", Turret.I_MOTION_MAGIC));
			SmartDashboard.putNumber("Turret D Operator Control", SmartDashboard.getNumber("Turret D Operator Control", Turret.D_MOTION_MAGIC));
			
			Turret.getInstance().setPID(SmartDashboard.getNumber("Turret P Operator Control", Turret.P_OPERATOR_CONTROL), 
								     SmartDashboard.getNumber("Turret I Operator Control", Turret.I_OPERATOR_CONTROL), 
								     SmartDashboard.getNumber("Turret D Operator Control", Turret.D_OPERATOR_CONTROL));	
		}
	}
}

