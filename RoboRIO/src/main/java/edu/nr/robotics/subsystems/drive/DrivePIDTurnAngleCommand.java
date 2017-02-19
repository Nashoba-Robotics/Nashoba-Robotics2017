package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Type;

public class DrivePIDTurnAngleCommand extends NRCommand {

	Angle angle; // Angle to turn in degrees

	Angle initialAngle;
	Angle finishAngle;

	/**
	 * Degrees in which the robot needs to be to stop DrivePIDTurnAngleCommand
	 * 
	 * TODO: DrivePIDTurnAngleCommand: Get threshold to finish turning
	 */
	public static final Angle PID_TURN_ANGLE_THRESHOLD = Angle.ZERO;

	// TODO: PIDTurnCommand: Get p value
	public static final double P = 0;

	/**
	 * 
	 * @param angle
	 *            in degrees
	 */
	public DrivePIDTurnAngleCommand(Angle angle) {
		super(Drive.getInstance());
		this.angle = angle;
	}

	@Override
	public void onStart() {
		initialAngle = NavX.getInstance().getYaw();
		finishAngle = initialAngle.add(angle);
	}

	private Angle getAngleError() {
		return finishAngle.sub(NavX.getInstance().getYaw());
	}

	@Override
	public void onExecute() {
		double turn = getAngleError().get(Type.DEGREE) * P;
		Drive.getInstance().arcadeDrive(0, turn);
	}

	@Override
	public boolean isFinishedNR() {
		return Math.abs(getAngleError().get(Type.DEGREE)) <= 
				DrivePIDTurnAngleCommand.PID_TURN_ANGLE_THRESHOLD.get(Type.DEGREE);
	}
}
