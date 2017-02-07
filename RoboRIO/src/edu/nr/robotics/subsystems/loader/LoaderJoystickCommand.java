package edu.nr.robotics.subsystems.loader;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;

public class LoaderJoystickCommand extends JoystickCommand {

	public long joystickCheckPeriod = 0; //TODO: Loader: Find period of checking for switching to joystick control
	
	public LoaderJoystickCommand() {
		super(Loader.getInstance());
	}
	
	@Override
	public void onExecute() {
		Loader.getInstance().setMotorSpeed(OI.getInstance().getLoaderValue());
	}

	@Override
	public boolean shouldSwitchToJoystick() {
		return OI.getInstance().isLoaderNonZero();
	}

	@Override
	public long getPeriodOfCheckingForSwitchToJoystick() {
		return joystickCheckPeriod; 
	}

}
