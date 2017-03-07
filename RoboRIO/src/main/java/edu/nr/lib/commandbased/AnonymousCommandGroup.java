package edu.nr.lib.commandbased;

import edu.wpi.first.wpilibj.command.CommandGroup;

public abstract class AnonymousCommandGroup extends CommandGroup {

	public AnonymousCommandGroup() {
		commands();
	}
	
	public abstract void commands();
	
}
