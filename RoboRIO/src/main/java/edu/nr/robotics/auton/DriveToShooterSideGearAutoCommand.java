package edu.nr.robotics.auton;

import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveToShooterSideGearAutoCommand extends CommandGroup {

	public DriveToShooterSideGearAutoCommand() {
		addParallel(new ZeroThenAutoTrackCommand());
		if (Robot.side == SideOfField.blue) {
			addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG, FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.negate(), FieldMap.ANGLE_TO_SIDE_PEG.negate()));
		}
		else {
			addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG, FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG, FieldMap.ANGLE_TO_SIDE_PEG));
		}
		addSequential(new GearPegAlignCommand());
		if (Robot.autoShoot) {
			addSequential(new AlignThenShootCommand());
		}
	}
}
