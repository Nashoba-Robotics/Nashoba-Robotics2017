package edu.nr.robotics.auton;

import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveConstantSpeedCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.command.WaitUntilCommand;

public class DriveToHopperAutoCommand extends CommandGroup {

	/**
	 * Seconds
	 */
	static double TIME_DRIVING_INTO_HOPPER = 0.5; 
	
	static double SPEED_DRIVING_INTO_HOPPER = 0.5;
	
	public DriveToHopperAutoCommand() {
		addParallel(new ZeroThenAutoTrackCommand());
		if (Robot.side == SideOfField.blue) {
			addSequential(new MotionProfileWallToHopperCommand(FieldMap.FORWARD_DISTANCE_WALL_TO_HOPPER, FieldMap.SIDE_DISTANCE_WALL_TO_HOPPER, FieldMap.ANGLE_WALL_TO_HOPPER));
		} else {
			addSequential(new MotionProfileWallToHopperCommand(FieldMap.FORWARD_DISTANCE_WALL_TO_HOPPER, -FieldMap.SIDE_DISTANCE_WALL_TO_HOPPER, -FieldMap.ANGLE_WALL_TO_HOPPER));
		}
		addParallel(new DriveConstantSpeedCommand(SPEED_DRIVING_INTO_HOPPER,SPEED_DRIVING_INTO_HOPPER));
		addSequential(new WaitCommand(TIME_DRIVING_INTO_HOPPER));
		addSequential(new DoNothingCommand(Drive.getInstance()));
		if (Robot.autoShoot)
			addSequential(new AlignThenShootCommand());
	}
}
