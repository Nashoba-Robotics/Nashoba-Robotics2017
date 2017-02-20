package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSetPIDSmartDashboardCommand extends NRCommand {

	public ShooterSetPIDSmartDashboardCommand() {
		requires(Shooter.getInstance());
	}
	
	@Override
	protected void onStart() {
		SmartDashboard.putNumber("Shooter P", SmartDashboard.getNumber("Shooter P", Shooter.P));
		SmartDashboard.putNumber("Shooter I", SmartDashboard.getNumber("Shooter I", Shooter.I));
		SmartDashboard.putNumber("Shooter D", SmartDashboard.getNumber("Shooter D", Shooter.D));
		SmartDashboard.putNumber("Shooter F", SmartDashboard.getNumber("Shooter F", Shooter.F));
		
		Shooter.getInstance().setPID(SmartDashboard.getNumber("Shooter P", Shooter.P), 
								     SmartDashboard.getNumber("Shooter I", Shooter.I), 
								     SmartDashboard.getNumber("Shooter D", Shooter.D), 
								     SmartDashboard.getNumber("Shooter F", Shooter.F));
	}
    
}

