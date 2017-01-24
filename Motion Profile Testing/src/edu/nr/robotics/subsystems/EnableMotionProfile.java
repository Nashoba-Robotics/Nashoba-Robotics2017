package edu.nr.robotics.subsystems;

import edu.nr.lib.NRCommand;

public class EnableMotionProfile extends NRCommand {

	public EnableMotionProfile() {
		super(Drive.getInstance());
	}
	
	@Override
	public void onStart() {
		Drive.getInstance().enableProfiler();
	}
	
	@Override
	public void onExecute() {
		System.out.println("Profiler enabled: " + Drive.getInstance().isProfilerEnabled());
	}

	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
