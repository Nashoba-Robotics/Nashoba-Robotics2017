package edu.nr.lib;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class NRSubsystem extends Subsystem {

	public static ArrayList<NRSubsystem> subsystems = new ArrayList<>();

	public abstract void disable();
	
	JoystickCommand joystickCommand;
	
	public NRSubsystem(JoystickCommand joystickCommand) {
		NRSubsystem.subsystems.add(this);
		this.joystickCommand = joystickCommand;
	}
	
	/**
	 * Chooses the default command when the Drive class is initialized
	 */
	@Override
	protected final void initDefaultCommand() {
		setDefaultCommand(joystickCommand);
	}

}
