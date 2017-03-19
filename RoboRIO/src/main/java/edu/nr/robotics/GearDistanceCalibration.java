package edu.nr.robotics;

public class GearDistanceCalibration {

	static final int[][] savedData = {{24,184}, {26,190}, {28,196}, {30,200}, {32,204}, {34,208}, {36,212}, {38,215}, {40,218}, {42,220}, {44,223}, {46,224}, {49,226}, {52,228}, {54,230}, {56,232}, {58,234}, {60,236}, {62,238}, {64,239}, {66,240}, {70,241}, {76,242}, {81,243}, {84,244}, {87,245}, {90,246}, {92,247}, {95,248}}; 
	
	public static double get(int num_px) {
		int beforeNum = 0;
		int beforeDist = 0;
		int afterNum = 0;
		int afterDist = 0;
		if(num_px < savedData[0][0]) {
			System.err.println("Tried to interpolate outside of range: num: " + num_px);
			return -1;
		}
		for(int i = 0; i < savedData.length; i++) {
			int x = savedData[i][1];
			int val = savedData[i][0];
			if(x > num_px) {
				afterDist = val;
				afterNum = x;
				break;
			}
			beforeDist = val;
			beforeNum = x;
		}
		
		double timeRatio = ((double) num_px - (double) beforeNum)/((double) afterNum - (double) beforeNum);
		System.out.println("Time ratio: " + timeRatio);
		return interpolate(beforeDist, afterDist, timeRatio);
			
	}
	
	private static double interpolate(double first, double second, double timeRatio) {
		return first * (1 - timeRatio) + second * timeRatio;
	}

	
}
