package edu.nr.robotics.auton;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.subsystems.hood.HoodStationaryAngleCorrectionCommand;
import edu.nr.robotics.subsystems.loader.LoaderShootCommand;
import edu.nr.robotics.subsystems.shooter.ShooterStationarySpeedCorrectionCommand;
import edu.nr.robotics.subsystems.turret.TurretStationaryAngleCorrectionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoShootCommand extends CommandGroup {

	public AutoShootCommand() {
		HoodStationaryAngleCorrectionCommand hoodCommand = new HoodStationaryAngleCorrectionCommand();
		TurretStationaryAngleCorrectionCommand turretCommand = new TurretStationaryAngleCorrectionCommand();
		ShooterStationarySpeedCorrectionCommand shooterCommand = new ShooterStationarySpeedCorrectionCommand();
		addParallel(hoodCommand);
		addParallel(turretCommand);
		addParallel(shooterCommand);
		addSequential(new NRCommand() {
			@Override
			public boolean isFinishedNR() {
				return !hoodCommand.isRunning() && !turretCommand.isRunning() && !shooterCommand.isRunning();
			}
		});
		addSequential(new LoaderShootCommand());
	}
}
