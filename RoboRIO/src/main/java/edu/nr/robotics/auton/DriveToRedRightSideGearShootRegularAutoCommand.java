package edu.nr.robotics.auton;

import edu.nr.lib.commandbased.AnonymousCommandGroup;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleExtendableCommand;
import edu.nr.robotics.subsystems.loader.LoaderRunCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class DriveToRedRightSideGearShootRegularAutoCommand extends CommandGroup {

	public DriveToRedRightSideGearShootRegularAutoCommand() {
		addSequential(new RequiredAutoCommand());
		
		
		addSequential(new DriveForwardProfilingCommand(new Distance(-78, Distance.Unit.INCH),0.5));
		addSequential(new DrivePIDTurnAngleExtendableCommand() {
			@Override
			public Angle getAngleToTurn() {
				return new Angle(60, Angle.Unit.DEGREE);
			}
		});
		
		addParallel(new AnonymousCommandGroup() {

			@Override
			public void commands() {
				addSequential(new WaitCommand(4));

				addParallel(new EnableAutoTrackingCommandAuton());
				
				addSequential(new WaitCommand(2));
				
				addSequential(new LoaderRunCommand());
				
			}
			
		});
		
		addSequential(new WaitCommand(1.5));
		
		addSequential(new GearPegAlignCommand());		

	}
}
