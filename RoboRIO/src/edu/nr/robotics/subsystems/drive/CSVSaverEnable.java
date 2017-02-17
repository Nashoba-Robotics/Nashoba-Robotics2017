package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;

public class CSVSaverEnable extends NRCommand {

	public void onStart() {
		CSVSaver.getInstance().enable();
	}
	
}
