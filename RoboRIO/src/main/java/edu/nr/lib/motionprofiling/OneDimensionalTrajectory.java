package edu.nr.lib.motionprofiling;

public interface OneDimensionalTrajectory {

	public double getGoalVelocity(double time);
	
	public double getGoalPosition(double time);

	public double getGoalAccel(double time);
	
	public double getGoalHeading(double time);

	public double getMaxPossibleVelocity();
	
	public double getMaxUsedVelocity();

	public double getMaxUsedAccel();

	public double getEndPosition();
}
