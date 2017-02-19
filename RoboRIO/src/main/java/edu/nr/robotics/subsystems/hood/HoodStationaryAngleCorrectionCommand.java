package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.StationaryTrackingCalculation;

public class HoodStationaryAngleCorrectionCommand extends NRCommand {
	
	public HoodStationaryAngleCorrectionCommand() {
		super(Hood.getInstance());
	}
	
	@Override
	public void onStart() {
		Hood.getInstance().setAutoAlign(true);
	}
	
	@Override
	public void onExecute() {
		Hood.getInstance().setPosition(StationaryTrackingCalculation.getInstance().getHoodAngle());
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
	@Override
	public void onEnd() {
		Hood.getInstance().setAutoAlign(false);
	}
}
