package edu.nr.robotics.auton;

import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.AutoDecideShootCommand;
import edu.nr.robotics.multicommands.AutoTrackingCalculation;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.nr.robotics.subsystems.hood.HoodStationaryAngleCorrectionCommand;
import edu.nr.robotics.subsystems.loader.LoaderShootCommand;
import edu.nr.robotics.subsystems.shooter.ShooterStationarySpeedCorrectionCommand;
import edu.nr.robotics.subsystems.turret.TurretStationaryAngleCorrectionCommand;

public class DriveToMiddleGearAutoCommand extends RequiredAutoCommand {

	public DriveToMiddleGearAutoCommand() {
		super();
		addParallel(new EnableAutoTrackingCommand());
		addSequential(new DriveForwardCommand(RobotMap.DISTANCE_TO_CENTER_PEG));
		if (Robot.autoShoot) {
			addSequential(new AutoShootCommand());
		}
	}
}
