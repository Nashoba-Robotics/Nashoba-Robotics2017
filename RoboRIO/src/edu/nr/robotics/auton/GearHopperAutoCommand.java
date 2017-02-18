package edu.nr.robotics.auton;

import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.AutoDecideShootCommand;
import edu.nr.robotics.multicommands.AutoTrackingCalculation;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.nr.robotics.subsystems.hood.HoodStationaryAngleCorrectionCommand;
import edu.nr.robotics.subsystems.loader.LoaderShootCommand;
import edu.nr.robotics.subsystems.shooter.ShooterStationarySpeedCorrectionCommand;
import edu.nr.robotics.subsystems.turret.TurretStationaryAngleCorrectionCommand;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class GearHopperAutoCommand extends CommandGroup {

	//TODO: GearHopperAutoCommand: Get distance to drive backward after gear is dropped off
	static final double BACKWARD_DRIVE_DISTANCE = 0; //Will be negative
	
	// TODO: GearHopperAutoCommand: Get time to delay while gear is dropped off
	static final double GEAR_SECONDS_TO_DELAY = 0; // In seconds
	
	public GearHopperAutoCommand() {
		super();
		if (Robot.side == SideOfField.blue) {
			addSequential(new MotionProfileToSideGearCommand(RobotMap.FORWARD_DISTANCE_TO_SIDE_PEG, RobotMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG, RobotMap.ANGLE_TO_SIDE_PEG));
		}
		else {
			addSequential(new MotionProfileToSideGearCommand(RobotMap.FORWARD_DISTANCE_TO_SIDE_PEG, -RobotMap.SIDE_DISTANCE_TO_SHOOTER_SIDE_PEG, -RobotMap.ANGLE_TO_SIDE_PEG));
		}
		addSequential(new GearPegAlignCommand());
		Timer.delay(GEAR_SECONDS_TO_DELAY);
		addSequential(new DriveForwardCommand(BACKWARD_DRIVE_DISTANCE));
		if (Robot.side == SideOfField.blue) {
			addSequential(new DrivePIDTurnAngleCommand(-RobotMap.ANGLE_TO_SIDE_PEG));
		} else {
			addSequential(new DrivePIDTurnAngleCommand(RobotMap.ANGLE_TO_SIDE_PEG));
		}
		addSequential(new DriveForwardCommand(RobotMap.GEAR_TO_HOPPER_FORWARD_DIST + BACKWARD_DRIVE_DISTANCE * Math.cos(RobotMap.ANGLE_TO_SIDE_PEG)));
		if (Robot.side == SideOfField.blue) {
			addSequential(new DrivePIDTurnAngleCommand(-RobotMap.RIGHT_ANGLE));
		} else {
			addSequential(new DrivePIDTurnAngleCommand(RobotMap.RIGHT_ANGLE));
		}
		addParallel(new EnableAutoTrackingCommand());
		addSequential(new DriveForwardCommand(RobotMap.GEAR_TO_HOPPER_SIDE_DIST - BACKWARD_DRIVE_DISTANCE * Math.sin(RobotMap.ANGLE_TO_SIDE_PEG)));
		if (Robot.autoShoot)
			addSequential(new AutoShootCommand());
	}
}
