package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.OI;
import edu.nr.robotics.Robot;
import edu.wpi.first.wpilibj.Compressor;

public class ClimbCommand extends NRCommand {

	private static final double MAX_CURRENT = 0; //TODO: Climber: Find max current
	private static final double MIN_CURRENT = 0; //TODO: Climber: Find min current
	
	/**
	 * Percent voltage
	 */
	private static final double CLIMB_VOLTAGE = 0; //TODO: Climber: Find climb speed

	public ClimbCommand() {
		super(Drive.getInstance());
	}
	
	@Override
	public void onStart() {
		Robot.robotCompressor.stop();
		Drive.getInstance().startDumbDrive();
	}
	
	@Override
	public void onExecute() {
		if(Drive.getInstance().getRightCurrent() > MAX_CURRENT) {
			Drive.getInstance().setMotorSpeedInPercent(0, 0);
		} else if(Drive.getInstance().getRightCurrent() < MIN_CURRENT) {
			Drive.getInstance().setMotorSpeedInPercent(0, CLIMB_VOLTAGE);
		}
	}
	
	@Override
	public void onEnd() {
		Robot.robotCompressor.start();
		if(!OI.getInstance().shouldDumbDrive()) {
			Drive.getInstance().endDumbDrive();
		}
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
}
