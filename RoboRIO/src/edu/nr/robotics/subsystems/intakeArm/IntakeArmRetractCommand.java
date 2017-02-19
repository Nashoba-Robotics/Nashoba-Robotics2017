package edu.nr.robotics.subsystems.intakeArm;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.subsystems.intake.Intake;

public class IntakeArmRetractCommand extends NRCommand {

	public IntakeArmRetractCommand() {
		super(IntakeArm.getInstance());
	}
	
	public void onStart() {
		Intake.getInstance().onIntakeArmRetract();
		IntakeArm.getInstance().retractIntakeArm();
	}
	
}
