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
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class GearHopperAutoCommand extends CommandGroup {

	//TODO: GearHopperAutoCommand: Get distance to drive backward after gear is dropped off
	private static final Distance BACKWARD_DRIVE_DISTANCE = Distance.ZERO; //Will be negative
	
	// TODO: GearHopperAutoCommand: Get time to delay while gear is dropped off
	private static final Time GEAR_SECONDS_TO_DELAY = new Time(5, Time.Unit.SECOND);
	
	public GearHopperAutoCommand() {
		addParallel(new ZeroThenAutoTrackCommand());
		if (Robot.side == SideOfField.blue) {
			addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG, FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG, FieldMap.ANGLE_TO_SIDE_PEG));
		}
		else {
			addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG, FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.negate(), FieldMap.ANGLE_TO_SIDE_PEG.negate()));
		}
		addSequential(new GearPegAlignCommand());
		addSequential(new WaitCommand(GEAR_SECONDS_TO_DELAY.get(Time.Unit.SECOND)));
		addSequential(new DriveForwardCommand(BACKWARD_DRIVE_DISTANCE));
		if (Robot.side == SideOfField.blue) {
			addSequential(new DrivePIDTurnAngleCommand(FieldMap.ANGLE_TO_SIDE_PEG.mul(-1)));
		} else {
			addSequential(new DrivePIDTurnAngleCommand(FieldMap.ANGLE_TO_SIDE_PEG));
		}
		addSequential(new DriveForwardCommand(FieldMap.GEAR_TO_HOPPER_FORWARD_DIST.add(BACKWARD_DRIVE_DISTANCE.mul(FieldMap.ANGLE_TO_SIDE_PEG.cos()))));
		if (Robot.side == SideOfField.blue) {
			addSequential(new DrivePIDTurnAngleCommand(Units.RIGHT_ANGLE.mul(-1)));
		} else {
			addSequential(new DrivePIDTurnAngleCommand(Units.RIGHT_ANGLE));
		}
		addSequential(new DriveForwardCommand(FieldMap.GEAR_TO_HOPPER_SIDE_DIST.add(BACKWARD_DRIVE_DISTANCE.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin()))));
		addParallel(new DriveConstantSpeedCommand(DriveToHopperAutoCommand.SPEED_DRIVING_INTO_HOPPER,DriveToHopperAutoCommand.SPEED_DRIVING_INTO_HOPPER));
		addSequential(new WaitCommand(DriveToHopperAutoCommand.TIME_DRIVING_INTO_HOPPER));
		addSequential(new DoNothingCommand(Drive.getInstance()));
		if (Robot.autoShoot)
			addSequential(new AlignThenShootCommand());
	}
}
