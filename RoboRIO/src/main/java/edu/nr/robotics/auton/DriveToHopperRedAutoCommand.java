package edu.nr.robotics.auton;

import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Time;
import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveConstantSpeedCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardPIDCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleExtendableCommand;
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
		addSequential(new RequiredAutoCommand());
	
		addSequential(new DriveForwardProfilingCommand((FieldMap.FORWARD_DISTANCE_WALL_TO_HOPPER.sub(Drive.WHEEL_BASE.mul(0.5))).negate()));
		addSequential(new DrivePIDTurnAngleExtendableCommand() {
			@Override
			public Angle getAngleToTurn() {
				return FieldMap.ANGLE_WALL_TO_HOPPER;
			}
		});
		addSequential(new DriveForwardProfilingCommand((FieldMap.SIDE_DISTANCE_WALL_TO_HOPPER.sub(Drive.WHEEL_BASE.mul(0.5)).sub(FieldMap.STOP_DISTANCE_FROM_HOPPER)).negate()));

		addParallel(new EnableAutoTrackingCommand());
		
		addParallel(new DriveConstantSpeedCommand(PERCENT_DRIVING_INTO_HOPPER, PERCENT_DRIVING_INTO_HOPPER)); 

		if (AutoMoveMethods.hopperRamStopMethod == HopperRamStopMethod.current) {
			addSequential(new DriveCurrentWaitCommand(MAX_CURRENT_INTO_HOPPER));
		} else {
			addSequential(new WaitCommand(TIME_DRIVING_INTO_HOPPER.get(Time.Unit.SECOND)));
		}
		addSequential(new DoNothingCommand(Drive.getInstance()));
		
		addSequential(new AlignThenShootCommand());
	}
}
