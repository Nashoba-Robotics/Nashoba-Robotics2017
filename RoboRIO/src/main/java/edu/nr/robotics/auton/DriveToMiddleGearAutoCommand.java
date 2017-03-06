package edu.nr.robotics.auton;

import edu.nr.lib.units.Distance;
import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardBasicCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveToMiddleGearAutoCommand extends CommandGroup {
	
	public DriveToMiddleGearAutoCommand() {
		if (Robot.autoShoot) {
			addParallel(new ZeroThenAutoTrackCommand());
		} else {
			addParallel(new RequiredAutoCommand());
		}
		if (AutoMoveMethods.gearAlignMethod == GearAlignMethod.profiling) {
			if (AutoMoveMethods.autoTravelMethod == AutoTravelMethod.basic) {
				addSequential(new DriveForwardBasicCommand(DriveOverBaselineAutoCommand.FORWARD_PERCENT, (FieldMap.DISTANCE_TO_CENTER_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.DRIVE_DEPTH_ON_PEG_FROM_SHIP)).negate()));
			} else {
				addSequential(new DriveForwardProfilingCommand((FieldMap.DISTANCE_TO_CENTER_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.DRIVE_DEPTH_ON_PEG_FROM_SHIP)).negate())); //Negated for driving backwards in auto
			}
		} else {
			addSequential(new DriveForwardProfilingCommand((FieldMap.DISTANCE_TO_CENTER_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.DRIVE_DEPTH_ON_PEG_FROM_SHIP).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG)).negate())); //Negated for driving backwards in auto
			addSequential(new GearPegAlignCommand());
		}
		if (Robot.autoShoot) {
			addSequential(new AlignThenShootCommand());
		}
	}
}
