package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.multicommands.AutoTrackingCalculationCommand;

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
		Shooter.getInstance().setMotorSpeed(AutoTrackingCalculationCommand.getShooterSpeed());
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
