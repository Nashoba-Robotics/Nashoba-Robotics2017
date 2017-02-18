package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.AutoTrackingCalculation;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodAutoAlignCommand;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.shooter.ShooterAutoAlignCommand;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.nr.robotics.subsystems.turret.TurretAutoAlignCommand;


public class EnableAutoTrackingCommand extends NRCommand{

	public EnableAutoTrackingCommand() {
		super(new NRSubsystem[] {Hood.getInstance(), Turret.getInstance(), Shooter.getInstance()});
	}
	
	@Override
	public void onExecute() {
		if (!AutoTrackingCalculation.getInstance().canSeeTarget()) {
			Turret.getInstance().setMotorSpeedInDegreesPerSecond(Turret.MAX_SPEED * Turret.MAX_TRACKING_PERCENTAGE * Turret.getInstance().turretTrackDirection);
		}
	}
	
	@Override
	public void onEnd() {
		Turret.getInstance().setPositionDelta(0);
		new HoodAutoAlignCommand().start();
		new ShooterAutoAlignCommand().start();
		new TurretAutoAlignCommand().start();
	}
	
	@Override
	public boolean isFinishedNR() {
		return AutoTrackingCalculation.getInstance().canSeeTarget();
	}
}
