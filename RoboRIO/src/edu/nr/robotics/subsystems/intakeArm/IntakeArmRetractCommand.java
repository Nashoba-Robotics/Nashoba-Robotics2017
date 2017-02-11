package edu.nr.robotics.subsystems.intakeArm;

import edu.nr.lib.commandbased.NRCommand;

public class IntakeArmRetractCommand extends NRCommand {

	public IntakeArmRetractCommand() {
		super(IntakeArm.getInstance());
	}
	
	public void onStart() {
		IntakeArm.getInstance().retractIntakeArm();
	}
	
}
