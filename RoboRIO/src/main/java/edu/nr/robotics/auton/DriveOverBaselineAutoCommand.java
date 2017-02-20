package edu.nr.robotics.auton;

import edu.nr.lib.units.Distance;
import edu.nr.robotics.Robot;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveOverBaselineAutoCommand extends CommandGroup {

	/**
	 * The distance (in inches) to drive to get over baseline safely in auto.
	 * 
	 * It needs to be at least 62 inches, but more is safer.
	 */
	public static final Distance DISTANCE_TO_GET_OVER_BASELINE = new Distance(65, Distance.Unit.INCH);
	
	public DriveOverBaselineAutoCommand() {
		addParallel(new ZeroThenAutoTrackCommand());
		addSequential(new DriveForwardCommand(DISTANCE_TO_GET_OVER_BASELINE));
		if (Robot.autoShoot) {
			addSequential(new AlignThenShootCommand());
		}
	}
}
