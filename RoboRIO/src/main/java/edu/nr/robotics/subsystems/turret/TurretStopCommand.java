package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class TurretStopCommand extends NRCommand{
	
	public TurretStopCommand() {
		super(Turret.getInstance());
	}

	@Override
	public void onStart() {
		Turret.getInstance().disable();
	}
	
}
