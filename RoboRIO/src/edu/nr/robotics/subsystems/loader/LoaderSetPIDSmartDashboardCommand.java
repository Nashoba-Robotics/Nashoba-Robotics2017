package edu.nr.robotics.subsystems.loader;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.subsystems.loader.Loader;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LoaderSetPIDSmartDashboardCommand extends NRCommand{

	public LoaderSetPIDSmartDashboardCommand() {
		requires(Loader.getInstance());
	}
	
	@Override
	protected void onStart() {
		SmartDashboard.putNumber("Loader P", SmartDashboard.getNumber("Loader P", Loader.P));
		SmartDashboard.putNumber("Loader I", SmartDashboard.getNumber("Loader I", Loader.I));
		SmartDashboard.putNumber("Loader D", SmartDashboard.getNumber("Loader D", Loader.D));
		SmartDashboard.putNumber("Loader F", SmartDashboard.getNumber("Loader F", Loader.F));
		
		Loader.getInstance().setPID(SmartDashboard.getNumber("Loader P", Loader.P), 
								     SmartDashboard.getNumber("Loader I", Loader.I), 
								     SmartDashboard.getNumber("Loader D", Loader.D), 
								     SmartDashboard.getNumber("Loader F", Loader.F));
	}
}
