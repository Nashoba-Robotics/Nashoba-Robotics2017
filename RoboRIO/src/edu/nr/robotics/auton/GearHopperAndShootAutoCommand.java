package edu.nr.robotics.auton;

import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.AutoTrackingCalculationCommand;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class GearHopperAndShootAutoCommand extends CommandGroup {

	//TODO: GearHopperAndShootAutoCommand: Get distance to drive backward after gear is dropped off
	static final double BACKWARD_DRIVE_DISTANCE = 0; //Will be negative
	
	//TODO: GearHopperAndShootAutoCommand: Get angle to turn after backing up
	static final double FIRST_TURN_ANGLE = 45; //Angle to turn to the right to get to perpendicular-to-wall position in degrees
	
	// TODO: GearHopperAndShootAutoCommand: Get time to delay while gear is dropped off
	static final double GEAR_SECONDS_TO_DELAY = 0; // In seconds
	
	//TODO: GearHopperAndShootAutoCommand: Make me!
	public GearHopperAndShootAutoCommand() {
		super();
		addParallel(new AutoTrackingCalculationCommand());
		addParallel(new EnableAutoTrackingCommand());
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
			addSequential(new DrivePIDTurnAngleCommand(-FIRST_TURN_ANGLE));
		} else {
			addSequential(new DrivePIDTurnAngleCommand(FIRST_TURN_ANGLE));
		}
		//Start here with trig for how far to drive once driving backward tomorrow
	}
}
