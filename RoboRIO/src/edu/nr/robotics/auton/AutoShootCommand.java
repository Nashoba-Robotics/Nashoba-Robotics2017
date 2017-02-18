package edu.nr.robotics.auton;

import edu.nr.robotics.subsystems.hood.HoodStationaryAngleCorrectionCommand;
import edu.nr.robotics.subsystems.loader.LoaderShootCommand;
import edu.nr.robotics.subsystems.shooter.ShooterStationarySpeedCorrectionCommand;
import edu.nr.robotics.subsystems.turret.TurretStationaryAngleCorrectionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoShootCommand extends CommandGroup {

	public AutoShootCommand() {
		addParallel(new HoodStationaryAngleCorrectionCommand());
		addParallel(new TurretStationaryAngleCorrectionCommand());
		addParallel(new ShooterStationarySpeedCorrectionCommand());
		addSequential(new LoaderShootCommand());
	}
}
