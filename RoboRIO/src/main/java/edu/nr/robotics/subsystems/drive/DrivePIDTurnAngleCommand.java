package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;

public class DrivePIDTurnAngleCommand extends NRCommand {

	Angle deltaAngle;

	Angle initialAngle;
	Angle finishAngle;

	/**
	 * Degrees in which the robot needs to be to stop DrivePIDTurnAngleCommand
	 */
	public static final Angle PID_TURN_ANGLE_THRESHOLD = new Angle(1, Angle.Unit.DEGREE);

	// TODO: PIDTurnCommand: Get p value
	public static final double P = 0;

	/**
	 * 
	 * @param angle
	 *            in degrees
	 */
	public DrivePIDTurnAngleCommand(Angle angle) {
		super(Drive.getInstance());
		this.deltaAngle = angle;
	}
	
	private Angle getCurrentAngle() {
		return NavX.getInstance().getYaw();
	}

	@Override
	public void onStart() {
		initialAngle = getCurrentAngle();
		finishAngle = initialAngle.add(deltaAngle);
	}

	private Angle getAngleError() {
		return finishAngle.sub(getCurrentAngle());
	}

	@Override
	public void onExecute() {
		double turn = getAngleError().get(Unit.DEGREE) * P;
		Drive.getInstance().arcadeDrive(0, turn);
	}

	@Override
	public boolean isFinishedNR() {
		return Math.abs(getAngleError().get(Unit.DEGREE)) <= 
				DrivePIDTurnAngleCommand.PID_TURN_ANGLE_THRESHOLD.get(Unit.DEGREE);
	}
}
