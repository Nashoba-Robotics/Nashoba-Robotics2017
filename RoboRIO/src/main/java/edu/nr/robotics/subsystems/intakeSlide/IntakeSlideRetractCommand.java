package edu.nr.robotics.subsystems.intakeSlide;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.subsystems.intake.Intake;

public class IntakeSlideRetractCommand extends NRCommand {

	public IntakeSlideRetractCommand() {
		super(IntakeSlide.getInstance());
	}
	
	public void onStart() {
		Intake.getInstance().onIntakeArmRetract();
		IntakeSlide.getInstance().retractIntakeSlide();
	}
	
}
