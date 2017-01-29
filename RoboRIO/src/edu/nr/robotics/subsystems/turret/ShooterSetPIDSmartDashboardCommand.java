package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSetPIDSmartDashboardCommand extends NRCommand {

	@Override
	protected void onStart() {
		SmartDashboard.putNumber("Turret P", SmartDashboard.getNumber("Turret P", Turret.P));
		SmartDashboard.putNumber("Turret I", SmartDashboard.getNumber("Turret I", Turret.I));
		SmartDashboard.putNumber("Turret D", SmartDashboard.getNumber("Turret D", Turret.D));
		
		Turret.getInstance().setPID(SmartDashboard.getNumber("Turret P", Turret.P), 
								     SmartDashboard.getNumber("Turret I", Turret.I), 
								     SmartDashboard.getNumber("Turret D", Turret.D));
	}
    
}

