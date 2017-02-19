package edu.nr.lib.motionprofiling;

public interface TwoDimensionalMotionProfiler {

	public void run();
	
	/**
	 * Stop the profiler from running
	 */
	public void disable();
	
	/**
	 * Start the profiler running
	 */
	public void enable();
	
	/**
	 * Reset the controller.
	 * Doesn't disable the controller
	 */
	public void reset();
	
	/**
	 * Sets the trajectory for the profiler
	 * @param trajectory
	 */
	public void setTrajectory(TwoDimensionalTrajectory trajectory);

	public boolean isEnabled();

	public TwoDimensionalTrajectory getTrajectory();
}
