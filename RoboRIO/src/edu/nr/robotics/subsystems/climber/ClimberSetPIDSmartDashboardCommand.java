package edu.nr.robotics.subsystems.climber;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.subsystems.climber.Climber;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ClimberSetPIDSmartDashboardCommand extends NRCommand {

	public ClimberSetPIDSmartDashboardCommand() {
		requires(Climber.getInstance());
	}
	
	@Override
	protected void onStart() {
		SmartDashboard.putNumber("Climber P", SmartDashboard.getNumber("Climber P", Climber.P));
		SmartDashboard.putNumber("Climber I", SmartDashboard.getNumber("Climber I", Climber.I));
		SmartDashboard.putNumber("Climber D", SmartDashboard.getNumber("Climber D", Climber.D));
		SmartDashboard.putNumber("Climber F", SmartDashboard.getNumber("Climber F", Climber.F));
		
		Climber.getInstance().setPID(SmartDashboard.getNumber("Climber P", Climber.P), 
								     SmartDashboard.getNumber("Climber I", Climber.I), 
								     SmartDashboard.getNumber("Climber D", Climber.D), 
								     SmartDashboard.getNumber("Climber F", Climber.F));
	}
}
