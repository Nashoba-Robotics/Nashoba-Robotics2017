package edu.nr.robotics.auton;

public class AutoMoveMethods {

	//TODO: AutoMoveMethods: CHECK THESE VALUES BEFORE EVERY MATCH
	
	/**
	 * The way we get around in autonomous mode
	 */
	public static final AutoTravelMethod autoTravelMethod = AutoTravelMethod.OneDProfilingAndPID;
	
	/**
	 * The way we stop ramming into the hopper (time or current)
	 */
	public static final HopperRamStopMethod hopperRamStopMethod = HopperRamStopMethod.current;
	
	/**
	 * The way we get to the gear peg (camera or profiling)
	 */
	public static final GearAlignMethod gearAlignMethod = GearAlignMethod.profiling;
	
	public static final ShootAlignMode shootAlignMode = ShootAlignMode.manual;
}
