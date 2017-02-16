package edu.nr.robotics.auton;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.AutoTrackingCalculationCommand;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.nr.robotics.subsystems.hood.HoodStationaryAngleCorrectionCommand;
import edu.nr.robotics.subsystems.shooter.ShooterStationarySpeedCorrectionCommand;
import edu.nr.robotics.subsystems.turret.TurretStationaryAngleCorrectionCommand;

public class CenterGearAndShootAutoCommand extends RequiredAutoCommand {
	
	//TODO: CenterGearAndShootCommand: Make me!
	public CenterGearAndShootAutoCommand() {
		super();
		addParallel(new AutoTrackingCalculationCommand());
		addParallel(new EnableAutoTrackingCommand());
		addSequential(new DriveForwardCommand(RobotMap.DISTANCE_TO_CENTER_PEG));
		addParallel(new TurretStationaryAngleCorrectionCommand());
		addParallel(new HoodStationaryAngleCorrectionCommand());
		addParallel(new ShooterStationarySpeedCorrectionCommand());
	}
}
