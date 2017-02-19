package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class IntakeSpeedCommand extends NRCommand {

	double speed;
	
	/**
	 * 
	 * @param speed Percent voltage
	 */
	public IntakeSpeedCommand(double speed) {
		super(Intake.getInstance());
		this.speed = speed;
	}
	
	@Override
	public void onStart() {
		Intake.getInstance().setMotorVoltage(speed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
