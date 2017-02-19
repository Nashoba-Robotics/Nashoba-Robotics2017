package edu.nr.robotics.subsystems.intakeArm;

import edu.nr.lib.commandbased.NRCommand;

public class IntakeArmDeployCommand extends NRCommand {

	public IntakeArmDeployCommand() {
		super(IntakeArm.getInstance());
	}
	
	public void onStart() {
		IntakeArm.getInstance().deployIntakeArm();
	}
	
}
