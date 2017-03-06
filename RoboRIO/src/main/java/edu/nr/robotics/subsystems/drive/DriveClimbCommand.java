package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.OI;
import edu.nr.robotics.Robot;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.turret.Turret;

public class DriveClimbCommand extends NRCommand {

	private static final double MAX_CURRENT = 0; //TODO: Climber: Find spike current
	
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
		Drive.getInstance().startDumbDrive();
		Drive.getInstance().setVoltageRampRate(3.6);
	}
	
	@Override
	public void onExecute() {
		if(Drive.getInstance().getLeftCurrent() > MAX_CURRENT) {
			doingInitialClimb = false;
		}
		
		if(doingInitialClimb) {
			Drive.getInstance().setMotorSpeedInPercent(0, CLIMB_VOLTAGE);
		} else {
			Drive.getInstance().setMotorSpeedInPercent(0, STAY_VOLTAGE);
		}
	}
	
	@Override
	public void onEnd() {
		Robot.robotCompressor.start();
		if(!OI.getInstance().shouldDumbDrive()) {
			Drive.getInstance().endDumbDrive();
		}
		Drive.getInstance().setVoltageRampRate(0.0);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
}