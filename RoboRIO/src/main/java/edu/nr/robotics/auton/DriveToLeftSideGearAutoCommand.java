package edu.nr.robotics.auton;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.FieldMap;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveForwardPIDCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveToLeftSideGearAutoCommand extends CommandGroup {

	public DriveToLeftSideGearAutoCommand() {
		if (Robot.autoShoot) {
			addParallel(new ZeroThenAutoTrackCommand());
		} else {
			addParallel(new RequiredAutoCommand());
		}
		
		addSequential(new DriveForwardProfilingCommand(new Distance(-85, Distance.Unit.INCH)));
		addSequential(new DrivePIDTurnAngleCommand(new Angle(-60, Angle.Unit.DEGREE)));

		if (AutoMoveMethods.gearAlignMethod == GearAlignMethod.camera) {
			addSequential(new GearPegAlignCommand());
		}
		
		if (Robot.autoShoot) {
			addSequential(new AlignThenShootCommand());
		}

		
		
	}
}
