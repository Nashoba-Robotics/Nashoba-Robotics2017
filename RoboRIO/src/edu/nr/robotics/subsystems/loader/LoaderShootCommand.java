package edu.nr.robotics.subsystems.loader;

import edu.nr.lib.Units;
import edu.nr.robotics.AutoTrackingCalculation;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodStationaryAngleCorrectionCommand;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.shooter.ShooterStationarySpeedCorrectionCommand;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.nr.robotics.subsystems.turret.TurretStationaryAngleCorrectionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

public class LoaderShootCommand extends CommandGroup {

	public LoaderShootCommand() {
		addSequential(new ConditionalCommand(new LoaderRunCommand()) {

			@Override
			protected boolean condition() {
				if (Math.abs(AutoTrackingCalculation.getInstance().getHoodAngle() - Hood.getInstance().getPosition()) < Hood.SHOOT_THRESHOLD / Units.DEGREES_PER_ROTATION
						&& Math.abs(AutoTrackingCalculation.getInstance().getShooterSpeed() - Shooter.getInstance().getSpeed()) < Shooter.SHOOT_THRESHOLD
						&& Math.abs(AutoTrackingCalculation.getInstance().getTurretAngle() - Turret.getInstance().getPosition()) < Turret.SHOOT_THRESHOLD / Units.DEGREES_PER_ROTATION) {
					return true;
				} else if (Math.abs(HoodStationaryAngleCorrectionCommand.getHoodAngle() - Hood.getInstance().getPosition()) < Hood.SHOOT_THRESHOLD / Units.DEGREES_PER_ROTATION
						&& Math.abs(ShooterStationarySpeedCorrectionCommand.getShooterSpeed() - Shooter.getInstance().getSpeed()) < Shooter.SHOOT_THRESHOLD
						&& Math.abs(TurretStationaryAngleCorrectionCommand.getTurretAngle() - Turret.getInstance().getPosition()) < Turret.SHOOT_THRESHOLD / Units.DEGREES_PER_ROTATION) {
					return true;
				}
				return false;			
			}
			
		});
	}
}
