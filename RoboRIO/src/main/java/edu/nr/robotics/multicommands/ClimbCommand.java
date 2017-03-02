package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.OI;
import edu.nr.robotics.Robot;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.turret.Turret;

public class ClimbCommand extends NRCommand {

	private static final double MAX_CURRENT = 0; //TODO: Climber: Find max current
	private static final double MIN_CURRENT = 0; //TODO: Climber: Find min current
	
	/**
	 * Percent voltage
	 */
	private static final double CLIMB_VOLTAGE = 0; //TODO: Climber: Find climb percent voltage

	public ClimbCommand() {
		super(new NRSubsystem[] {Drive.getInstance(), Turret.getInstance()});
	}
	
	@Override
	public void onStart() {
		if ((Turret.getInstance().getPosition().sub(Turret.REVERSE_POSITION)).abs().lessThan(Turret.getInstance().getPosition().sub(Turret.FORWARD_POSITION).abs())) {
			Turret.getInstance().setPosition(Turret.REVERSE_POSITION);
		} else {
			Turret.getInstance().setPosition(Turret.FORWARD_POSITION);
		}
		Turret.getInstance().setPosition(Turret.REVERSE_POSITION);
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
