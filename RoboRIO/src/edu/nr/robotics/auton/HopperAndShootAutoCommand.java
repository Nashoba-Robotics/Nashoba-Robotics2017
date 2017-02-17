package edu.nr.robotics.auton;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.AutoDecideShootCommand;
import edu.nr.robotics.multicommands.AutoTrackingCalculationCommand;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.subsystems.hood.HoodStationaryAngleCorrectionCommand;
import edu.nr.robotics.subsystems.shooter.ShooterStationarySpeedCorrectionCommand;
import edu.nr.robotics.subsystems.turret.TurretStationaryAngleCorrectionCommand;

public class HopperAndShootAutoCommand extends RequiredAutoCommand {

	public HopperAndShootAutoCommand() {
		super();
		addParallel(new AutoTrackingCalculationCommand());
		addParallel(new EnableAutoTrackingCommand());
		addSequential(new MotionProfileWallToHopperCommand(RobotMap.FORWARD_DISTANCE_WALL_TO_HOPPER, RobotMap.SIDE_DISTANCE_WALL_TO_HOPPER, RobotMap.ANGLE_WALL_TO_HOPPER));
		addParallel(new TurretStationaryAngleCorrectionCommand());
		addParallel(new HoodStationaryAngleCorrectionCommand());
		addParallel(new ShooterStationarySpeedCorrectionCommand());
		addSequential(new AutoDecideShootCommand());
	}
}
