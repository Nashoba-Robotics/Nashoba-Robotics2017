package edu.nr.robotics.auton;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveConstantSpeedCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class GearHopperAutoCommand extends CommandGroup {

	//TODO: GearHopperAutoCommand: Get distance to drive backward after gear is dropped off
	private static final Distance BACKWARD_DRIVE_DISTANCE = Distance.ZERO; //Will be negative
	
	// TODO: GearHopperAutoCommand: Get time to delay while gear is dropped off
	private static final Time GEAR_SECONDS_TO_DELAY = new Time(5, Time.Unit.SECOND);
	
	public GearHopperAutoCommand() {
		if (Robot.autoShoot) {
			addParallel(new ZeroThenAutoTrackCommand());
		} else {
			addParallel(new RequiredAutoCommand());
		}
		if (Robot.side == SideOfField.blue) {
			addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG, FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG, FieldMap.ANGLE_TO_SIDE_PEG, true));
		}
		else {
			addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG, FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.negate(), FieldMap.ANGLE_TO_SIDE_PEG.negate(), true));
		}
		addSequential(new GearPegAlignCommand());
		addSequential(new WaitCommand(GEAR_SECONDS_TO_DELAY.get(Time.Unit.SECOND)));
		addSequential(new DriveForwardProfilingCommand(BACKWARD_DRIVE_DISTANCE.negate())); //Negated to go backwards in auto
		if (Robot.side == SideOfField.blue) {
			addSequential(new DrivePIDTurnAngleCommand(Units.RIGHT_ANGLE.negate().add(FieldMap.ANGLE_TO_SIDE_PEG)));
		} else {
			addSequential(new DrivePIDTurnAngleCommand(FieldMap.ANGLE_TO_SIDE_PEG.add(Units.RIGHT_ANGLE.negate())));
		}
		addSequential(new DriveForwardProfilingCommand(FieldMap.GEAR_TO_HOPPER_SIDE_DIST.add(BACKWARD_DRIVE_DISTANCE.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin()))));
		addParallel(new DriveConstantSpeedCommand(DriveToHopperAutoCommand.PERCENT_DRIVING_INTO_HOPPER,DriveToHopperAutoCommand.PERCENT_DRIVING_INTO_HOPPER));
		addSequential(new DriveCurrentWaitCommand(DriveToHopperAutoCommand.MAX_CURRENT_INTO_HOPPER));
		//addSequential(new WaitCommand(TIME_DRIVING_INTO_HOPPER));
		addSequential(new DoNothingCommand(Drive.getInstance()));
		if (Robot.autoShoot)
			addSequential(new AlignThenShootCommand());
	}
}
