package edu.nr.lib.motionprofiling;

public class OneDimensionalTrajectorySimple implements OneDimensionalTrajectory {
	
	double maxPossibleVelocity;
	double maxUsedVelocity;
	double maxUsedAccel;
	
	double totalTime;
	double timeAccelPlus;
	double timeAccelMinus;
	double timeAtCruise;
	
	double endPosition;
	double startPosition;
	
	boolean triangleShaped;
		
	public OneDimensionalTrajectorySimple(double goalPositionDelta, double maxPossibleVelocity, double maxUsedVelocity, double maxUsedAccel) {
		this.endPosition = goalPositionDelta;
		this.startPosition = 0;
		if(goalPositionDelta < 0) {
			maxUsedVelocity *= -1;
			maxPossibleVelocity *= -1;
			maxUsedAccel *= -1; 
		}
		
		this.maxUsedVelocity = maxUsedVelocity;
		this.maxPossibleVelocity = maxPossibleVelocity;
		this.maxUsedAccel = maxUsedAccel; 
		
		timeAccelPlus = timeAccelMinus = maxUsedVelocity / maxUsedAccel;
		if(Math.abs(0.5 * (timeAccelPlus + timeAccelMinus) * maxUsedVelocity) >= Math.abs(goalPositionDelta)) {
			triangleShaped = true;
			timeAtCruise = 0;
			timeAccelPlus = timeAccelMinus = Math.sqrt(goalPositionDelta / maxUsedAccel);
		} else {
			triangleShaped = false;
			double cruiseDistance = goalPositionDelta - 0.5 * timeAccelPlus * maxUsedVelocity - 0.5 * timeAccelMinus * maxUsedVelocity;
			timeAtCruise = cruiseDistance / maxUsedVelocity;
		}
		
		System.out.println("Triangle shaped: " + triangleShaped);
		
		totalTime = timeAccelMinus + timeAtCruise + timeAccelMinus;
	}

	public double getGoalVelocity(double time) {
		
		if(triangleShaped) {
			if(time <= 0) return 0;
			if(time <= timeAccelPlus) return time * maxUsedAccel;
			double timeSlowingDownSoFar = time - timeAccelPlus;
			if(time <= totalTime) return maxUsedVelocity + timeSlowingDownSoFar * -maxUsedAccel;
			return 0;
		} else {
			if(time <= 0) return 0;
			if(time <= timeAccelPlus) return time * maxUsedAccel;
			if(time <= timeAccelPlus + timeAtCruise) return maxUsedVelocity;
			double timeSlowingDownSoFar = time - (timeAccelPlus + timeAtCruise);
			if(time <= totalTime) return maxUsedVelocity + timeSlowingDownSoFar * -maxUsedAccel;
			return 0;
		}
	}
	
	public double getGoalPosition(double time) {	
		if(time > totalTime) {
			return endPosition;
		}
		if(triangleShaped) {
			if(time <= timeAccelPlus) {
				//We're on the positive slope of the trapezoid
				return 0.5 * time * time * maxUsedAccel + startPosition;
			}
			
			double speedUpDistance =  0.5 * timeAccelPlus * maxUsedVelocity;
			
			if(time <= totalTime) {
				//We're on the negative slope of the trapezoid
				double timeSlowingDownSoFar = time - (timeAccelPlus + timeAtCruise);
				return speedUpDistance 
						+ maxUsedVelocity * timeSlowingDownSoFar 
						- 0.5 * maxUsedAccel * timeSlowingDownSoFar * timeSlowingDownSoFar + startPosition;
			}
					
			return endPosition;
		} else  {
			if(time <= timeAccelPlus) {
				//We're on the positive slope of the trapezoid
				return 0.5 * time * time * maxUsedAccel + startPosition;
			}
			
			double speedUpDistance =  0.5 * timeAccelPlus * maxUsedVelocity;
			
			if(time <= timeAccelPlus + timeAtCruise && timeAtCruise > 0) {
				//We're on the top part of the trapezoid
				double timeAtFullSoFar = time - timeAccelPlus;
				return speedUpDistance + timeAtFullSoFar * maxUsedVelocity + startPosition;
			}
			
			double fullSpeedDistance = maxUsedVelocity * timeAtCruise;
			
			if(time <= totalTime) {
				//We're on the negative slope of the trapezoid
				double timeSlowingDownSoFar = time - (timeAccelPlus + timeAtCruise);
				return speedUpDistance + fullSpeedDistance 
						+ maxUsedVelocity * timeSlowingDownSoFar 
						- 0.5 * maxUsedAccel * timeSlowingDownSoFar * timeSlowingDownSoFar + startPosition;
			}
					
			return endPosition;
		}
	}

	public double getGoalAccel(double time) {
		if(time < 0) return 0;
		if(time < timeAccelPlus) return maxUsedAccel;
		if(time < timeAccelPlus + timeAtCruise && !triangleShaped) return 0;
		if(time < totalTime) return -maxUsedAccel;
		return 0;
	}
	
	public double getMaxUsedVelocity() {
		return maxUsedVelocity;
	}

	public double getMaxAccel() {
		return maxUsedAccel;
	}

	@Override
	public double getMaxPossibleVelocity() {
		return maxPossibleVelocity;
	}

	@Override
	public double getMaxUsedAccel() {
		return maxUsedAccel;
	}

	@Override
	public double getEndPosition() {
		return endPosition;
	}

	@Override
	public double getGoalHeading(double time) {
		return 0;
	}

	
}