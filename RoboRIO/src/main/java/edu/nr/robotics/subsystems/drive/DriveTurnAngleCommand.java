package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class DriveTurnAngleCommand extends NRCommand {

	Angle deltaAngle;
	
	Angle initialAngle;
	Angle finalAngle;
	private static final double TURN_PERCENTAGE = 0.5;
	
	public static final Angle TURN_ANGLE_THRESHOLD = new Angle(1, Angle.Unit.DEGREE);
	
	public DriveTurnAngleCommand(Angle angle) {
		super(Drive.getInstance());
		this.deltaAngle = angle;
	}
	
	@Override
	public void onStart() {
		initialAngle = NavX.getInstance().getYaw();
		finalAngle = initialAngle.add(deltaAngle);
	}
	
	@Override
	public void onExecute() {
		Drive.getInstance().arcadeDrive(0, TURN_PERCENTAGE * deltaAngle.signum());
	}
	
	@Override
	public void onEnd() {
		Drive.getInstance().disable();
	}
	
	@Override
	public boolean isFinishedNR() {
		return (NavX.getInstance().getYaw().sub(finalAngle)).abs().lessThan(TURN_ANGLE_THRESHOLD);
	}
}
