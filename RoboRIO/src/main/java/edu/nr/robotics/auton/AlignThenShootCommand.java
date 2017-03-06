package edu.nr.robotics.auton;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.robotics.FieldMap;
import edu.nr.robotics.subsystems.hood.HoodPositionCommand;
import edu.nr.robotics.subsystems.hood.HoodStationaryAngleCorrectionCommand;
import edu.nr.robotics.subsystems.loader.LoaderShootCommand;
import edu.nr.robotics.subsystems.shooter.ShooterSpeedCommand;
import edu.nr.robotics.subsystems.shooter.ShooterStationarySpeedCorrectionCommand;
import edu.nr.robotics.subsystems.turret.TurretStationaryAngleCorrectionCommand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AlignThenShootCommand extends CommandGroup {

	public static final Angle hoodAngle = Angle.ZERO;
	public static final AngularSpeed shooterSpeed = AngularSpeed.ZERO;
	
	public AlignThenShootCommand() {
		Command hoodCommand;
		Command turretCommand;
		Command shooterCommand;
		if (AutoMoveMethods.shootAlignMode == ShootAlignMode.autonomous) {
			hoodCommand = new HoodStationaryAngleCorrectionCommand();
			shooterCommand = new ShooterStationarySpeedCorrectionCommand();
		} else {
			hoodCommand = new HoodPositionCommand(hoodAngle);
			shooterCommand = new ShooterSpeedCommand(shooterSpeed);
		}
		turretCommand = new TurretStationaryAngleCorrectionCommand();
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
