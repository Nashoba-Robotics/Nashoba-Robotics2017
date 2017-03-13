package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.subsystems.drive.Drive.Gear;

public class DriveLowGearCommand extends NRCommand {
	
	public DriveLowGearCommand() {
		super(Drive.getInstance());
	}
	
	@Override
	public void onStart() {
		Drive.getInstance().switchToLowGear();
	}
	
}
