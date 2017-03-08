package edu.nr.robotics.subsystems.compressor;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.Robot;

public class CompressorStopCommand extends NRCommand {

	public CompressorStopCommand() {
		
	}
	
	public void onStart() {
		Robot.robotCompressor.stop();
	}
}
