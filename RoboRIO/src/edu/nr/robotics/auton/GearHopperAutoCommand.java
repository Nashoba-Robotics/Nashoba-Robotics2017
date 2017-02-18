package edu.nr.robotics.auton;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.DoNothingCommand;
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
	private static final double BACKWARD_DRIVE_DISTANCE = 0; //Will be negative
	
	// TODO: GearHopperAutoCommand: Get time to delay while gear is dropped off
	private static final double GEAR_SECONDS_TO_DELAY = 0; // In seconds
	
	public GearHopperAutoCommand() {
		addParallel(new ZeroThenAutoTrackCommand());
		if (Robot.side == SideOfField.blue) {
			addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG, FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG, FieldMap.ANGLE_TO_SIDE_PEG));
		}
		else {
			addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG, -FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG, -FieldMap.ANGLE_TO_SIDE_PEG));
		}
		addSequential(new GearPegAlignCommand());
		addSequential(new WaitCommand(GEAR_SECONDS_TO_DELAY));
		addSequential(new DriveForwardCommand(BACKWARD_DRIVE_DISTANCE));
		if (Robot.side == SideOfField.blue) {
			addSequential(new DrivePIDTurnAngleCommand(-FieldMap.ANGLE_TO_SIDE_PEG));
		} else {
			addSequential(new DrivePIDTurnAngleCommand(FieldMap.ANGLE_TO_SIDE_PEG));
		}
		addSequential(new DriveForwardCommand(FieldMap.GEAR_TO_HOPPER_FORWARD_DIST + BACKWARD_DRIVE_DISTANCE * Math.cos(FieldMap.ANGLE_TO_SIDE_PEG)));
		if (Robot.side == SideOfField.blue) {
			addSequential(new DrivePIDTurnAngleCommand(-Units.RIGHT_ANGLE));
		} else {
			addSequential(new DrivePIDTurnAngleCommand(Units.RIGHT_ANGLE));
		}
		addSequential(new DriveForwardCommand(FieldMap.GEAR_TO_HOPPER_SIDE_DIST - BACKWARD_DRIVE_DISTANCE * Math.sin(FieldMap.ANGLE_TO_SIDE_PEG)));
		addParallel(new DriveConstantSpeedCommand(DriveToHopperAutoCommand.SPEED_DRIVING_INTO_HOPPER,DriveToHopperAutoCommand.SPEED_DRIVING_INTO_HOPPER));
		addSequential(new WaitCommand(DriveToHopperAutoCommand.TIME_DRIVING_INTO_HOPPER));
		addSequential(new DoNothingCommand(Drive.getInstance()));
		if (Robot.autoShoot)
			addSequential(new AlignThenShootCommand());
	}
}
