package edu.nr.robotics.subsystems.loader;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.AutoTrackingCalculation;
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
				if (Math.abs(AutoTrackingCalculation.getInstance().getHoodAngle() - Hood.getInstance().getPosition()) < RobotMap.SHOOT_HOOD_THRESHOLD / RobotMap.DEGREES_PER_ROTATION
						&& Math.abs(AutoTrackingCalculation.getInstance().getShooterSpeed() - Shooter.getInstance().getSpeed()) < RobotMap.SHOOT_SHOOTER_THRESHOLD
						&& Math.abs(AutoTrackingCalculation.getInstance().getTurretAngle() - Turret.getInstance().getPosition()) < RobotMap.SHOOT_TURRET_THRESHOLD / RobotMap.DEGREES_PER_ROTATION) {
					if (!Loader.getInstance().isRunning()) {
						return true;
					}
				} else if (Math.abs(HoodStationaryAngleCorrectionCommand.getHoodAngle() - Hood.getInstance().getPosition()) < RobotMap.SHOOT_HOOD_THRESHOLD / RobotMap.DEGREES_PER_ROTATION
						&& Math.abs(ShooterStationarySpeedCorrectionCommand.getShooterSpeed() - Shooter.getInstance().getSpeed()) < RobotMap.SHOOT_SHOOTER_THRESHOLD
						&& Math.abs(TurretStationaryAngleCorrectionCommand.getTurretAngle() - Turret.getInstance().getPosition()) < RobotMap.SHOOT_TURRET_THRESHOLD / RobotMap.DEGREES_PER_ROTATION) {
					if (!Loader.getInstance().isRunning()) {
						return true;
					}
				}
				return false;			
			}
			
		});
	}
}
