package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.AutoTrackingCalculation;
import edu.nr.robotics.auton.AutoMoveMethods;
import edu.nr.robotics.auton.ShootAlignMode;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodAutoAlignCommand;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.shooter.ShooterAutoAlignCommand;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.nr.robotics.subsystems.turret.TurretAutoAlignCommand;


public class EnableAutoTrackingCommand extends NRCommand{

	private static final Angle TURRET_CAMERA_RANGE = new Angle(10, Angle.Unit.DEGREE); 
	
	HoodAutoAlignCommand hoodAlignCommand;
	ShooterAutoAlignCommand shooterAlignCommand;
	TurretAutoAlignCommand turretAlignCommand;
	
	public EnableAutoTrackingCommand() {
		super(new NRSubsystem[] {Hood.getInstance(), Turret.getInstance(), Shooter.getInstance()});
		
		hoodAlignCommand = new HoodAutoAlignCommand();
		shooterAlignCommand = new ShooterAutoAlignCommand();
		turretAlignCommand = new TurretAutoAlignCommand();
	}
	
	@Override
	public void onExecute() {
		if (!AutoTrackingCalculation.getInstance().canSeeTarget()) {
			Turret.getInstance().setMotorSpeedInPercent(Turret.MAX_TRACKING_PERCENTAGE*Turret.getInstance().turretTrackDirection);
		}
	}
	
	@Override
	public void onEnd(boolean interrupted) {
		if(!interrupted) {
			Turret.getInstance().setPosition(Turret.getInstance().getPosition().add(TURRET_CAMERA_RANGE.mul(Turret.getInstance().turretTrackDirection)));
			Turret.getInstance().disable();
			if (AutoMoveMethods.shootAlignMode == ShootAlignMode.autonomous) {
				hoodAlignCommand.start();
				shooterAlignCommand.start();
			}
			turretAlignCommand.start();
		}
	}
	
	@Override
	public boolean isFinishedNR() {
		return AutoTrackingCalculation.getInstance().canSeeTarget();
	}
}
