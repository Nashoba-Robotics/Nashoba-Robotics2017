package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.multicommands.AutoTrackingCalculationCommand;

public class TurretAutoAlignCommand extends NRCommand {

	public TurretAutoAlignCommand() {
		super(Turret.getInstance());
	}
	
	@Override
	public void onStart() {
		Turret.getInstance().setAutoAlign(true);
	}
	
	@Override
	public void onExecute() {
		Turret.getInstance().setMotorSpeed(AutoTrackingCalculationCommand.getTurretAngle());
	}
	
	@Override
	public void onEnd() {
		Turret.getInstance().setAutoAlign(false);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
