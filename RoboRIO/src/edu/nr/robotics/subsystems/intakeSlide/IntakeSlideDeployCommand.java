package edu.nr.robotics.subsystems.intakeSlide;

import edu.nr.lib.commandbased.NRCommand;

public class IntakeSlideDeployCommand extends NRCommand {

	public IntakeSlideDeployCommand() {
		super(IntakeSlide.getInstance());
	}
	
	public void onStart() {
		IntakeSlide.getInstance().deployIntakeSlide();
	}
	
}
