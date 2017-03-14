package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;

public class DriveHighGearCommand extends NRCommand {
	
	public DriveHighGearCommand() {
		super(Drive.getInstance());
	}
	
	@Override
	public void onStart() {
		Drive.getInstance().switchToHighGear();
	}
	
}
