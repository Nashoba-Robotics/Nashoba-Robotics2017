package edu.nr.lib.interfaces;

import java.util.ArrayList;

/**
 * An interface for code to implement if they want to be called every loop of the code.
 * 
 * After implementing, they should add themselves to {@link SmartDashboardSource#sources} to actually be called.
 */
public interface SmartDashboardSource {
	
	/**
	 * A list of all the classes that should be called every loop of the code.
	 */
	public final static ArrayList<SmartDashboardSource> sources = new ArrayList<>();
	
	/**
	 * Runs every SmartDashboardSource class. This should only be called in one place in the code!
	 */
	public static void runAll() {
		sources.forEach(SmartDashboardSource::smartDashboardInfo);
	}
	
	/**
	 * This function is called every loop of the code
	 */
	public abstract void smartDashboardInfo();

}
