package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.OI;
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
		if (OI.getInstance().isShooterOn()) {
			Shooter.getInstance().setMotorSpeed(AutoTrackingCalculationCommand.getShooterSpeed());
		} else {
			Shooter.getInstance().setMotorSpeed(0);
		}
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
