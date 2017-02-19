package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.AutoTrackingCalculation;
import edu.nr.robotics.OI;

public class ShooterAutoAlignCommand extends NRCommand {

	public ShooterAutoAlignCommand() {
		super(Shooter.getInstance());
	}
	
	@Override
	public void onStart() {
		Shooter.getInstance().setAutoAlign(true);
	}
	
	@Override
	public void onExecute() {
		Shooter.getInstance().setMotorSpeedInRPM(AutoTrackingCalculation.getInstance().getShooterSpeed());
	}
	
	@Override
	public void onEnd() {
		Shooter.getInstance().setAutoAlign(false);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
