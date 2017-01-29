package edu.nr.lib.interfaces;

import java.util.ArrayList;

public interface SmartDashboardSource {
	
	public final static ArrayList<SmartDashboardSource> sources = new ArrayList<>();

	public static void runAll() {
		sources.forEach(SmartDashboardSource::smartDashboardInfo);
	}
	
	/**
	 * This function is called every loop of the code
	 */
	public abstract void smartDashboardInfo();

}
