package edu.nr.robotics.multicommands;

import edu.nr.robotics.GearAlignCalculation;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class GearPegAlignCommand extends CommandGroup {

    public GearPegAlignCommand() {
    	if (GearAlignCalculation.getInstance().canSeeTarget()) {
    		addSequential(new DrivePIDTurnAngleCommand(GearAlignCalculation.getInstance().getAngleToTurnDegrees()));
    		addSequential(new DriveForwardCommand(GearAlignCalculation.getInstance().getDistToDrive()));
    	}
    }
}
