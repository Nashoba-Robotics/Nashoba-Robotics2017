package edu.nr.robotics.auton;

import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.AutoDecideShootCommand;
import edu.nr.robotics.multicommands.AutoTrackingCalculationCommand;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.hood.HoodStationaryAngleCorrectionCommand;
import edu.nr.robotics.subsystems.loader.LoaderShootCommand;
import edu.nr.robotics.subsystems.shooter.ShooterStationarySpeedCorrectionCommand;
import edu.nr.robotics.subsystems.turret.TurretStationaryAngleCorrectionCommand;

public class DriveToShooterSideGearAutoCommand extends RequiredAutoCommand {

	public DriveToShooterSideGearAutoCommand() {
		super();
		addParallel(new AutoTrackingCalculationCommand());
		addParallel(new EnableAutoTrackingCommand());
		if (Robot.side == SideOfField.blue) {
			addSequential(new MotionProfileToSideGearCommand(RobotMap.FORWARD_DISTANCE_TO_SIDE_PEG, RobotMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG, RobotMap.ANGLE_TO_SIDE_PEG));
		}
		else {
			addSequential(new MotionProfileToSideGearCommand(RobotMap.FORWARD_DISTANCE_TO_SIDE_PEG, -RobotMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG, -RobotMap.ANGLE_TO_SIDE_PEG));
		}
		addSequential(new GearPegAlignCommand());
		if (Robot.autoShoot) {
			addSequential(new AutoShootCommand());
		}
	}
}
