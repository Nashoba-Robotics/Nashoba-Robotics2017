package edu.nr.lib.network;

public class AndroidData {

	public double turnAngle;
	public double distance;
	public long time;
	
	public AndroidData(double turnAngle, double distance, long time) {
		this.turnAngle = turnAngle;
		this.distance = distance;
		this.time = time;
	}
	
	public double getTurnAngle() {
		return turnAngle;
	}

	public double getDistance() {
		return distance;
	}

	public long getTime() {
		return time;
	}
	
}
