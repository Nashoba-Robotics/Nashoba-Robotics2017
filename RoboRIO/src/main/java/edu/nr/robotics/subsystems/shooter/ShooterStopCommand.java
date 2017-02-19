package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.units.AngularSpeed;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ShooterStopCommand extends CommandGroup{

	public ShooterStopCommand() {
		addSequential(new ShooterSpeedCommand(AngularSpeed.ZERO));
	}
	
}
