package edu.nr.robotics.auton;

import edu.nr.lib.units.Distance;
import edu.nr.robotics.Robot;
import edu.nr.robotics.subsystems.drive.DriveForwardBasicCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardPIDCommand;
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
	public static final double FORWARD_PERCENT = 0.25;
	
	public DriveOverBaselineAutoCommand() {
		addSequential(new RequiredAutoCommand());

		addSequential(new DriveForwardBasicCommand(-.6, new Distance(70, Distance.Unit.INCH)));

	}
}
