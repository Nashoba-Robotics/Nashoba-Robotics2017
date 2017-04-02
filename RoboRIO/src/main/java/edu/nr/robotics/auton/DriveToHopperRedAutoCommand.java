package edu.nr.robotics.auton;

import edu.nr.lib.commandbased.AnonymousCommandGroup;
import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveConstantSpeedCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardPIDCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.drive.DriveLowGearCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleExtendableCommand;
import edu.nr.robotics.subsystems.gearMover.GearDeployCommand;
import edu.nr.robotics.subsystems.loader.LoaderRunCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 * This will make the drive base go into the hopper on the intake side.
 * If the robot is motion profiling, it will go in gear-side.
 */
public class DriveToHopperRedAutoCommand extends CommandGroup {
	
	/**
	 * Seconds to ram into hopper
	 * 
	 * TODO: Find time to drive into hopper
	 */
	final static Time TIME_DRIVING_INTO_HOPPER = new Time(0, Time.Unit.SECOND); 
	
	/**
	 * Current at which ramming into hopper will stop
	 * 
	 * TODO: Find current at which to stop ramming into hopper
	 */
	final static double MAX_CURRENT_INTO_HOPPER = 0;
	
	/**
	 * Percent at which to ram into hopper
	 */
	final static double PERCENT_DRIVING_INTO_HOPPER = 1.0;
	
	public DriveToHopperRedAutoCommand() {
addSequential(new DriveLowGearCommand());
		
		addParallel(new AnonymousCommandGroup() {

			@Override
			public void commands() {
				addSequential(new WaitCommand(1));
				addSequential(new GearDeployCommand());
			}
			
		});
				
		addSequential(new DriveForwardProfilingCommand(new Distance(92.5, Distance.Unit.INCH),.75));
		addSequential(new DrivePIDTurnAngleExtendableCommand() {
			@Override
			public Angle getAngleToTurn() {
				return new Angle(-90, Angle.Unit.DEGREE);
			}
		});
		
		addParallel(new AnonymousCommandGroup() {

			@Override
			public void commands() {
				addSequential(new WaitCommand(1.5));

				addParallel(new EnableAutoTrackingCommandAuton());
				
				addSequential(new WaitCommand(2));
				
				addSequential(new LoaderRunCommand());
				
			}
			
		});
		
		
		addSequential(new DriveForwardProfilingCommand(new Distance(62, Distance.Unit.INCH),.5));
	}
}
