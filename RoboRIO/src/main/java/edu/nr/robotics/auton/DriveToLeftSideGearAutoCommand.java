package edu.nr.robotics.auton;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveToLeftSideGearAutoCommand extends CommandGroup {

	public DriveToLeftSideGearAutoCommand() {
		addParallel(new RequiredAutoCommand());
		
		addSequential(new DriveForwardProfilingCommand(new Distance(-89, Distance.Unit.INCH),0.5));
		addSequential(new DrivePIDTurnAngleCommand(new Angle(60, Angle.Unit.DEGREE)));

		addSequential(new GearPegAlignCommand());		
		
	}
}
