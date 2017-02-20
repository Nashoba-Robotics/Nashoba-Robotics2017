package edu.nr.lib.commandbased;

/**
 * A command that doesn't do anything
 *
 * This is used as, for example, a default autonomous mode 
 */
public class DoNothingCommand extends NRCommand {

	/**
	 * Run unconnected to any subsystem
	 */
	public DoNothingCommand() {
		super();
	}
	
	/**
	 * Run connected to a subsystem
	 * @param subsystem The subsystem to use
	 */
	public DoNothingCommand(NRSubsystem s) {
		super(s);
	}
	
}
