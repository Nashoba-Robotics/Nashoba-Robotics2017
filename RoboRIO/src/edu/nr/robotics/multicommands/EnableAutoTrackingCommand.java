package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.turret.Turret;

public class EnableAutoTrackingCommand extends NRCommand {

	public EnableAutoTrackingCommand() {
		
	}
	
	@Override
	public void onStart() {
		Hood.getInstance().setAutoAlign(true);
		Shooter.getInstance().setAutoAlign(true);
		Turret.getInstance().setAutoAlign(true);
	}
}
