package edu.nr.lib.motionprofiling;

public interface TwoDimensionalTrajectory {

	public double getLeftGoalVelocity(double time);

	public double getRightGoalVelocity(double time);

	public double getLeftGoalPosition(double time);

	public double getRightGoalPosition(double time);

	public double getLeftGoalAccel(double time);

	public double getRightGoalAccel(double time);
	
	public double getGoalHeading(double time);

	public double getLeftMaxPossibleVelocity();

	public double getRightMaxPossibleVelocity();
	
	public double getLeftMaxUsedVelocity();

	public double getLeftMaxUsedAccel();

	public double getRightMaxUsedVelocity();

	public double getRightMaxUsedAccel();

	public double getLeftEndPosition();

	public double getRightEndPosition();
}
