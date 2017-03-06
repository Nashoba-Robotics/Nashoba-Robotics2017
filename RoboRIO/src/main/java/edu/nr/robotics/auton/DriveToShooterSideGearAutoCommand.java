package edu.nr.robotics.auton;

import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveToShooterSideGearAutoCommand extends CommandGroup {

	public DriveToShooterSideGearAutoCommand() {
		if (Robot.autoShoot) {
			addParallel(new ZeroThenAutoTrackCommand());
		} else {
			addParallel(new RequiredAutoCommand());
		}
		
		if (Robot.side == SideOfField.red) {
			if (FieldMap.autoTravelMethod == AutoTravelMethod.twoDmotionProfiling) {
				if (FieldMap.gearAlignMethod == GearAlignMethod.camera) {
					addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.cos())), FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin())), FieldMap.ANGLE_TO_SIDE_PEG, true));
				} else {
					addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST), FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)), FieldMap.ANGLE_TO_SIDE_PEG, true));
				}
			} else {
				addSequential(new DriveForwardProfilingCommand((FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.cos()))).negate()));
				addSequential(new DrivePIDTurnAngleCommand(FieldMap.ANGLE_TO_SIDE_PEG.negate()));
				addSequential(new DriveForwardProfilingCommand((FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin()))).negate()));
			}
		}
		else {
			if (FieldMap.autoTravelMethod == AutoTravelMethod.twoDmotionProfiling) {
				if (FieldMap.gearAlignMethod == GearAlignMethod.camera) {
					addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.cos())), (FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin()))).negate(), (FieldMap.ANGLE_TO_SIDE_PEG).negate(), true));
				} else {
					addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST), (FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5))).negate(), (FieldMap.ANGLE_TO_SIDE_PEG).negate(), true));
				}
			} else {
				addSequential(new DriveForwardProfilingCommand((FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.cos()))).negate()));
				addSequential(new DrivePIDTurnAngleCommand(FieldMap.ANGLE_TO_SIDE_PEG));
				addSequential(new DriveForwardProfilingCommand((FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin()))).negate()));
			}
		}
		
		if (FieldMap.gearAlignMethod == GearAlignMethod.camera) {
			addSequential(new GearPegAlignCommand());
		}
		
		if (Robot.autoShoot) {
			addSequential(new AlignThenShootCommand());
		}
	}
}
