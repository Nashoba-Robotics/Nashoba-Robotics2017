package edu.nr.lib;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class NRSubsystem extends Subsystem {

	public static ArrayList<NRSubsystem> subsystems = new ArrayList<>();

	public abstract void disable();
	
	JoystickCommand joystickCommand;
	
	Timer switchToJoystickTimer;
	
	public NRSubsystem(JoystickCommand joystickCommand) {
		NRSubsystem.subsystems.add(this);
		this.joystickCommand = joystickCommand;
		
		switchToJoystickTimer = new Timer();
		switchToJoystickTimer.schedule(new JoystickSwitchChecker(), 0, joystickCommand.getPeriodOfCheckingForSwitchToJoystick());
	}
	
	/**
	 * Chooses the default command when the Drive class is initialized
	 */
	@Override
	protected final void initDefaultCommand() {
		setDefaultCommand(joystickCommand);
	}
	
	private class JoystickSwitchChecker extends TimerTask {

		@Override
		public void run() {
			if(joystickCommand.shouldSwitchToJoystick()) {
				Command currentCommand = getCurrentCommand();
				if(currentCommand != joystickCommand) {
					getCurrentCommand().cancel();
				}
			}
		}
		
	}

}
