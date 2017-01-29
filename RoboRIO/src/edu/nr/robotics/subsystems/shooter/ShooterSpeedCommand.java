package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.NRCommand;
import edu.nr.lib.interfaces.SmartDashboardSource;

public class ShooterSpeedCommand extends NRCommand implements SmartDashboardSource {

	public ShooterSpeedCommand() {
		requires(Shooter.getInstance());
	}
	
	@Override
	protected void onStart() {
		
	}
	
	@Override
	protected void onExecute() {
		
	}
	
	@Override
	public void smartDashboardInfo() {
		
	}

}
