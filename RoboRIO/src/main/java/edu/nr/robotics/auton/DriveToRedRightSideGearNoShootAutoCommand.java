package edu.nr.robotics.auton;

import edu.nr.lib.commandbased.AnonymousCommandGroup;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleExtendableCommand;
import edu.nr.robotics.subsystems.loader.LoaderRunCommand;
import edu.nr.robotics.subsystems.loader.LoaderStopCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class DriveToRedRightSideGearNoShootAutoCommand extends CommandGroup {

	public DriveToRedRightSideGearNoShootAutoCommand() {
		addSequential(new RequiredAutoCommand());
		
		
		addParallel(new EnableAutoTrackingCommandAuton());
		
		addSequential(new WaitCommand(1));
		addSequential(new LoaderRunCommand());
		addSequential(new WaitCommand(4));
		addSequential(new LoaderStopCommand());
		
		addSequential(new WaitCommand(1.5));
		
		addSequential(new GearPegAlignCommand());		

	}
}
