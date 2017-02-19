package edu.nr.robotics.auton;

import edu.nr.robotics.Robot;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveOverBaselineAutoCommand extends CommandGroup {

	/**
	 * The distance (in inches) to drive to get over baseline safely in auto
	 * 
	 * TODO: Autonomous: Get the optimal auto distance to get over baseline
	 */
	public static final double DISTANCE_TO_GET_OVER_BASELINE = 0;
	
	public DriveOverBaselineAutoCommand() {
		addParallel(new ZeroThenAutoTrackCommand());
		addSequential(new DriveForwardCommand(DISTANCE_TO_GET_OVER_BASELINE));
		if (Robot.autoShoot) {
			addSequential(new AlignThenShootCommand());
		}
	}
}
