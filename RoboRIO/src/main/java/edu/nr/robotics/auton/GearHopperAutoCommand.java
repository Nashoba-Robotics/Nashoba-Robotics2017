package edu.nr.robotics.auton;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveConstantSpeedCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardPIDCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class GearHopperAutoCommand extends CommandGroup {
	
	// TODO: GearHopperAutoCommand: Get time to delay while gear is dropped off
	private static final Time GEAR_SECONDS_TO_DELAY = new Time(5, Time.Unit.SECOND);
	
	public GearHopperAutoCommand() {
		if (Robot.autoShoot) {
			addParallel(new ZeroThenAutoTrackCommand());
		} else {
			addParallel(new RequiredAutoCommand());
		}
		
		//Code to drop off gear in auto
		if (Robot.side == SideOfField.red) {
			if (AutoMoveMethods.autoTravelMethod == AutoTravelMethod.twoDmotionProfiling) {
				if (AutoMoveMethods.gearAlignMethod == GearAlignMethod.camera) {
					addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.cos())), FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin())), FieldMap.ANGLE_TO_SIDE_PEG, true));
				} else {
					addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST), FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)), FieldMap.ANGLE_TO_SIDE_PEG, true));
				}
			} else if (AutoMoveMethods.autoTravelMethod == AutoTravelMethod.OneDProfilingAndPID) {
				addSequential(new DriveForwardProfilingCommand((FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.cos()))).negate()));
				addSequential(new DrivePIDTurnAngleCommand(FieldMap.ANGLE_TO_SIDE_PEG.negate()));
				addSequential(new DriveForwardProfilingCommand((FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin()))).negate()));
			} else {
				addSequential(new DriveForwardPIDCommand((FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.cos()))).negate()));
				addSequential(new DrivePIDTurnAngleCommand(FieldMap.ANGLE_TO_SIDE_PEG.negate()));
				addSequential(new DriveForwardPIDCommand((FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin()))).negate()));
			}
		}
		else {
			if (AutoMoveMethods.autoTravelMethod == AutoTravelMethod.twoDmotionProfiling) {
				if (AutoMoveMethods.gearAlignMethod == GearAlignMethod.camera) {
					addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.cos())), (FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin()))).negate(), (FieldMap.ANGLE_TO_SIDE_PEG).negate(), true));
				} else {
					addSequential(new MotionProfileToSideGearCommand(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST), (FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5))).negate(), (FieldMap.ANGLE_TO_SIDE_PEG).negate(), true));
				}
			} else if (AutoMoveMethods.autoTravelMethod == AutoTravelMethod.OneDProfilingAndPID) {
				addSequential(new DriveForwardProfilingCommand((FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.cos()))).negate()));
				addSequential(new DrivePIDTurnAngleCommand(FieldMap.ANGLE_TO_SIDE_PEG));
				addSequential(new DriveForwardProfilingCommand((FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin()))).negate()));
			} else {
				addSequential(new DriveForwardPIDCommand((FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.cos()))).negate()));
				addSequential(new DrivePIDTurnAngleCommand(FieldMap.ANGLE_TO_SIDE_PEG));
				addSequential(new DriveForwardPIDCommand((FieldMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG.add(Drive.WHEEL_BASE.mul(0.5)).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG.mul(FieldMap.ANGLE_TO_SIDE_PEG.sin()))).negate()));
			}
		}
		
		if (AutoMoveMethods.gearAlignMethod == GearAlignMethod.camera) {
			addSequential(new GearPegAlignCommand());
		}
		
		//Code to wait for gear to be lifted
		addSequential(new WaitCommand(GEAR_SECONDS_TO_DELAY.get(Time.Unit.SECOND)));
		
		if (AutoMoveMethods.autoTravelMethod == AutoTravelMethod.allPID) {
			addSequential(new DriveForwardPIDCommand(((FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(FieldMap.FORWARD_DISTANCE_WALL_TO_HOPPER)).mul(1 / FieldMap.ANGLE_TO_SIDE_PEG.cos()).sub(FieldMap.DRIVE_DEPTH_ON_PEG_FROM_SHIP).sub(RobotMap.GEAR_CAMERA_TO_CENTER_OF_ROBOT_DIST_Y)).negate()));
		} else {
			addSequential(new DriveForwardProfilingCommand(((FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(FieldMap.FORWARD_DISTANCE_WALL_TO_HOPPER)).mul(1 / FieldMap.ANGLE_TO_SIDE_PEG.cos()).sub(FieldMap.DRIVE_DEPTH_ON_PEG_FROM_SHIP).sub(RobotMap.GEAR_CAMERA_TO_CENTER_OF_ROBOT_DIST_Y)).negate()));
		}
		if (Robot.side == SideOfField.blue) {
			addSequential(new DrivePIDTurnAngleCommand(Units.RIGHT_ANGLE.add(FieldMap.ANGLE_TO_SIDE_PEG.negate())));
		} else {
			addSequential(new DrivePIDTurnAngleCommand(FieldMap.ANGLE_TO_SIDE_PEG.add(Units.RIGHT_ANGLE.negate())));
		}
		
		if (AutoMoveMethods.autoTravelMethod == AutoTravelMethod.allPID) {
			addSequential(new DriveForwardPIDCommand(FieldMap.GEAR_TO_HOPPER_SIDE_DIST.sub(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(FieldMap.FORWARD_DISTANCE_WALL_TO_HOPPER).mul(FieldMap.ANGLE_TO_SIDE_PEG.tan())).sub(FieldMap.STOP_DISTANCE_FROM_HOPPER)));
		} else {
			addSequential(new DriveForwardProfilingCommand(FieldMap.GEAR_TO_HOPPER_SIDE_DIST.sub(FieldMap.FORWARD_DISTANCE_TO_SIDE_PEG.sub(FieldMap.FORWARD_DISTANCE_WALL_TO_HOPPER).mul(FieldMap.ANGLE_TO_SIDE_PEG.tan())).sub(FieldMap.STOP_DISTANCE_FROM_HOPPER)));
		}
		
		addParallel(new DriveConstantSpeedCommand(DriveToHopperAutoCommand.PERCENT_DRIVING_INTO_HOPPER, DriveToHopperAutoCommand.PERCENT_DRIVING_INTO_HOPPER));
		if (AutoMoveMethods.hopperRamStopMethod == HopperRamStopMethod.current) {
			addSequential(new DriveCurrentWaitCommand(DriveToHopperAutoCommand.MAX_CURRENT_INTO_HOPPER));
		} else {
			addSequential(new WaitCommand(DriveToHopperAutoCommand.TIME_DRIVING_INTO_HOPPER.get(Time.Unit.SECOND)));
		}
		addSequential(new DoNothingCommand(Drive.getInstance()));
		if (Robot.autoShoot)
			addSequential(new AlignThenShootCommand());
	}
}
