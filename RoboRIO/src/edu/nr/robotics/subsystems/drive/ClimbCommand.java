package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;

public class ClimbCommand extends NRCommand {

	private static final double MAX_CURRENT = 0; //TODO: Climber: Find max current
	private static final double MIN_CURRENT = 0; //TODO: Climber: Find min current
	
	/**
	 * Percent voltage
	 */
	private static final double CLIMB_SPEED = 0; //TODO: Climber: Find climb speed

	public ClimbCommand() {
		super(Drive.getInstance());
	}
	
	@Override
	public void onStart() {
		Drive.getInstance().startDumbDrive();
	}
	
	@Override
	public void onExecute() {
		if(Drive.getInstance().getRightCurrent() > MAX_CURRENT) {
			Drive.getInstance().setMotorSpeedInPercent(0, 0);
		} else if(Drive.getInstance().getRightCurrent() < MIN_CURRENT) {
			Drive.getInstance().setMotorSpeedInPercent(0, CLIMB_SPEED);
		}
	}
	
	@Override
	public void onEnd() {
		Drive.getInstance().endDumbDrive();
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
}
