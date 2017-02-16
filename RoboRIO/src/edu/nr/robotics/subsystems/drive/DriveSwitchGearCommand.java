package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.subsystems.drive.Drive.Gear;

public class DriveSwitchGearCommand extends NRCommand {

	Gear gear;
	
	public DriveSwitchGearCommand(Gear gear) {
		super(Drive.getInstance());
		
		this.gear = gear;
	}
	
	//TODO: Drive: Does gear need to stop moving, then wait, then switch?
	public void onStart() {
		if(gear == Gear.high) {
			Drive.getInstance().switchToHighGear();
		} else {
			Drive.getInstance().switchToLowGear();
		}
	}
	
}
