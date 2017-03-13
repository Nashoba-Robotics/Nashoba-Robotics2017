package edu.nr.robotics.subsystems.compressor;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.Robot;

public class CompressorToggleCommand extends NRCommand {
	
	public void onStart() {
		if(Robot.robotCompressor.getClosedLoopControl()) {
			Robot.robotCompressor.stop();
		} else {
			Robot.robotCompressor.start();
		}
	}
}
