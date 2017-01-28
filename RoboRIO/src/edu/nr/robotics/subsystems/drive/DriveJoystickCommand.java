package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.JoystickCommand;
import edu.wpi.first.wpilibj.Joystick;

public class DriveJoystickCommand extends JoystickCommand {

	Joystick leftJoystick;
	Joystick rightJoystick;
	
	public DriveJoystickCommand(Joystick leftJoystick, Joystick rightJoystick) {
		super(Drive.getInstance());
		this.leftJoystick = leftJoystick;
		this.rightJoystick = rightJoystick;
	}
	
	@Override
	public void onExecute() {
		
		//TODO: Turn values from drive joysticks into move and turn for Drive.getInstance().arcadeDrive
		
		
	}

	@Override
	public boolean shouldSwitchToJoystick() {
		return false; //TODO: Should go if the joysticks are outside the threshold range.
	}

	@Override
	public long getPeriodOfCheckingForSwitchToJoystick() {
		return 100; //TODO find the best period
	}

}
