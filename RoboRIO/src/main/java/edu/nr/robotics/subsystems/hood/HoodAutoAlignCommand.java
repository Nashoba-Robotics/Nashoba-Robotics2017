package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.AutoTrackingCalculation;

public class HoodAutoAlignCommand extends NRCommand{

	public HoodAutoAlignCommand() {
		super(Hood.getInstance());
	}
	
	@Override
	public void onStart() {
		Hood.getInstance().setAutoAlign(true);
	}
	
	@Override
	public void onExecute() {
		Hood.getInstance().setPosition(AutoTrackingCalculation.getInstance().getHoodAngle());
	}
	
	@Override
	public void onEnd() {
		Hood.getInstance().setAutoAlign(false);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
