package edu.nr.robotics.auton;

import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.Robot;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveConstantSpeedCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardBasicCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveOverBaselineAutoCommand extends CommandGroup {

	/**
	 * The distance (in inches) to drive to get over baseline safely in auto.
	 * 
	 * It needs to be at least 62 inches, but more is safer.
	 */
	public static final Distance DISTANCE_TO_GET_OVER_BASELINE = new Distance(65, Distance.Unit.INCH);
	
	/**
	 * The speed in percent to drive forward to get over baseline safely in auto
	 */
	public static final double FORWARD_PERCENT = 0.5;
	
	public DriveOverBaselineAutoCommand() {
		addParallel(new ZeroThenAutoTrackCommand());
		addSequential(new DriveForwardBasicCommand(0.5, DISTANCE_TO_GET_OVER_BASELINE));
		//addSequential(new DriveForwardProfilingCommand(DISTANCE_TO_GET_OVER_BASELINE.negate())); //Negated to drive backwards in auto
		if (Robot.autoShoot) {
			addSequential(new AlignThenShootCommand());
		}
	}
}
