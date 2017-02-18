package edu.nr.robotics.subsystems.agitator;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;

public class AgitatorJoystickCommand extends JoystickCommand {

	public AgitatorJoystickCommand() {
		super(Agitator.getInstance());
	}
	
	@Override
	public void onExecute() {
		Agitator.getInstance().setMotorSpeed(RobotMap.AGITATOR_RUN_SPEED);
	}

	@Override
	protected boolean shouldSwitchToJoystick() {
		return false;
	}

	@Override
	protected long getPeriodOfCheckingForSwitchToJoystick() {
		return Long.MAX_VALUE;
	}
}
