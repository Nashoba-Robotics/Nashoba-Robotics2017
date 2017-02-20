package edu.nr.robotics.auton;

import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ZeroThenAutoTrackCommand extends CommandGroup {

    public ZeroThenAutoTrackCommand() {
    	addSequential(new RequiredAutoCommand());
		addSequential(new EnableAutoTrackingCommand());
    }
}
