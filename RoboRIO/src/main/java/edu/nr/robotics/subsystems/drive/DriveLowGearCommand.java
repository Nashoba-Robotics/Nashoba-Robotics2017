package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;

public class DriveLowGearCommand extends NRCommand {
	
	public DriveLowGearCommand() {
		super(Drive.getInstance());
	}
	
	@Override
	public void onStart() {
		Drive.getInstance().switchToLowGear();
	}
	
}
