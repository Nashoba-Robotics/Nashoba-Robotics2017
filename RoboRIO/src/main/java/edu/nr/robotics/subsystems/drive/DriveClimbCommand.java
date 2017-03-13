package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.OI;
import edu.nr.robotics.Robot;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.turret.Turret;

public class DriveClimbCommand extends NRCommand {

	private static final double MAX_CURRENT = 60; //TODO: Climber: Find spike current
	
	/**
	 * Percent voltage
	 */
	private static final double CLIMB_VOLTAGE = 1;
	private static final double STAY_VOLTAGE = .4;
	
	public boolean doingInitialClimb = true;

	public DriveClimbCommand() {
		super(Drive.getInstance());
	}
	
	@Override
	public void onStart() {
		doingInitialClimb = true;
		
		Robot.robotCompressor.stop();
		Drive.getInstance().switchToLowGear();
	}
	
	@Override
	public void onExecute() {
		if(Drive.getInstance().getRightCurrent() > MAX_CURRENT) {
			doingInitialClimb = false;
		}
		
		if(doingInitialClimb) {
			Drive.getInstance().setMotorSpeedInPercent(CLIMB_VOLTAGE, CLIMB_VOLTAGE);
		} else {
			Drive.getInstance().setMotorSpeedInPercent(STAY_VOLTAGE, STAY_VOLTAGE);
		}
	}
	
	@Override
	public void onEnd() {
		Robot.robotCompressor.start();
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
}
