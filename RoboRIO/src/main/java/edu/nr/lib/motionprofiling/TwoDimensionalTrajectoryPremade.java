package edu.nr.lib.motionprofiling;

import java.util.ArrayList;

public class TwoDimensionalTrajectoryPremade implements TwoDimensionalTrajectory {

	//The values in the position, velocity, and acceleration lists
	//are offset by the period.
	// That is, the first value in the list is at time 0,
	// the second value in the list is at time $period,
	// the third value in the list is at time 2*$period,
	// and so on.	
	private final double period;
	private final ArrayList<Double> leftPositionList;
	private final ArrayList<Double> leftVelocityList;
	private final ArrayList<Double> leftAccelerationList;

	private final ArrayList<Double> rightPositionList;
	private final ArrayList<Double> rightVelocityList;
	private final ArrayList<Double> rightAccelerationList;

	private final ArrayList<Double> headingList;

	private final double maxPossibleLeftVelocity;
	private final double maxPossibleRightVelocity;
	
	public TwoDimensionalTrajectoryPremade(double maxPossibleLeftVelocity, double maxPossibleRightVelocity, ArrayList<Double> leftPositionList, ArrayList<Double> leftVelocityList, ArrayList<Double> leftAccelerationList, ArrayList<Double> rightPositionList, ArrayList<Double> rightVelocityList, ArrayList<Double> rightAccelerationList, ArrayList<Double> headingList, double period) {
		this.maxPossibleLeftVelocity = maxPossibleLeftVelocity;
		this.maxPossibleRightVelocity = maxPossibleRightVelocity;
		this.leftPositionList = leftPositionList;
		this.leftVelocityList = leftVelocityList;
		this.leftAccelerationList = leftAccelerationList;
		this.rightPositionList = rightPositionList;
		this.rightVelocityList = rightVelocityList;
		this.rightAccelerationList = rightAccelerationList;
		this.headingList = headingList;
		this.period = period;
	}
	
	@Override
	public double getLeftGoalVelocity(double time) {
		if(time > getTotalTimeOfTrajectory()) {
			return getLeftEndVelocity();
		}
		return leftVelocityList.get((int) (time/period));
	}

	@Override
	public double getLeftGoalPosition(double time) {
		if(time > getTotalTimeOfTrajectory()) {
			return getLeftEndPosition();
		}
		return leftPositionList.get((int) (time/period));
	}

	@Override
	public double getLeftGoalAccel(double time) {
		if(time > getTotalTimeOfTrajectory()) {
			return getLeftEndAcceleration();
		}
		return leftAccelerationList.get((int) (time/period));
	}
	
	@Override
	public double getRightGoalVelocity(double time) {
		if(time > getTotalTimeOfTrajectory()) {
			return getRightEndVelocity();
		}
		return rightVelocityList.get((int) (time/period));
	}

	@Override
	public double getRightGoalPosition(double time) {
		if(time > getTotalTimeOfTrajectory()) {
			return getRightEndPosition();
		}
		return rightPositionList.get((int) (time/period));
	}

	@Override
	public double getRightGoalAccel(double time) {
		if(time > getTotalTimeOfTrajectory()) {
			return getRightEndAcceleration();
		}
		return rightAccelerationList.get((int) (time/period));
	}

	@Override
	public double getGoalHeading(double time) {
		if(time > getTotalTimeOfTrajectory()) {
			return getEndHeading();
		}
		return headingList.get((int) (time/period));
	}

	@Override
	public double getLeftMaxPossibleVelocity() {
		return maxPossibleLeftVelocity;
	}

	@Override
	public double getRightMaxPossibleVelocity() {
		return maxPossibleRightVelocity;
	}

	@Override
	public double getLeftMaxUsedVelocity() {
		double maxVelocity = 0;
		for(double d : leftVelocityList) {
			if(d > maxVelocity) {
				maxVelocity = d;
			}
		}
		return maxVelocity;
	}

	@Override
	public double getLeftMaxUsedAccel() {
		double maxAccel = 0;
		for(double d : leftAccelerationList) {
			if(d > maxAccel) {
				maxAccel = d;
			}
		}
		return maxAccel;
	}

	@Override
	public double getRightMaxUsedVelocity() {
		double maxVelocity = 0;
		for(double d : rightVelocityList) {
			if(d > maxVelocity) {
				maxVelocity = d;
			}
		}
		return maxVelocity;
	}

	@Override
	public double getRightMaxUsedAccel() {
		double maxAccel = 0;
		for(double d : rightAccelerationList) {
			if(d > maxAccel) {
				maxAccel = d;
			}
		}
		return maxAccel;
	}
	
	public double getTotalTimeOfTrajectory() {
		return Math.min(Math.min(
				Math.min(leftPositionList.size() - 1, 
						Math.min(leftVelocityList.size() - 1, 
								leftAccelerationList.size() - 1)),
				Math.min(rightPositionList.size() - 1, 
						Math.min(rightVelocityList.size() - 1, 
								rightAccelerationList.size() - 1))),
				headingList.size() -1)
				* period;
	}
	
	public double getLeftEndAcceleration() {
		return leftAccelerationList.get(leftAccelerationList.size() - 1);
	}
	
	public double getLeftEndVelocity() {
		return leftVelocityList.get(leftVelocityList.size() - 1);
	}

	@Override
	public double getLeftEndPosition() {
		return leftPositionList.get(leftPositionList.size() - 1);
	}
	
	public double getRightEndAcceleration() {
		return rightAccelerationList.get(rightAccelerationList.size() - 1);
	}
	
	public double getRightEndVelocity() {
		return rightVelocityList.get(rightVelocityList.size() - 1);
	}

	@Override
	public double getRightEndPosition() {
		return rightPositionList.get(rightPositionList.size() - 1);
	}
	
	public double getEndHeading() {
		return headingList.get(headingList.size() - 1);
	}

}
