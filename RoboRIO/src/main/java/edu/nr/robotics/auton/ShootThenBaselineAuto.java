package edu.nr.robotics.auton;

import edu.nr.lib.units.Distance;
import edu.nr.robotics.subsystems.drive.DriveForwardBasicCommand;
import edu.nr.robotics.subsystems.gearMover.GearDeployCommand;
import edu.nr.robotics.subsystems.loader.LoaderRunCommand;
import edu.nr.robotics.subsystems.loader.LoaderStopCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ShootThenBaselineAuto extends CommandGroup {
	
	public ShootThenBaselineAuto() {
		addSequential(new GearDeployCommand());
		addSequential(new WaitCommand(1));
		addSequential(new LoaderRunCommand());
		addSequential(new WaitCommand(8));
		addSequential(new LoaderStopCommand());
		addSequential(new DriveForwardBasicCommand(-.6, new Distance(70, Distance.Unit.INCH)));
	}

}
