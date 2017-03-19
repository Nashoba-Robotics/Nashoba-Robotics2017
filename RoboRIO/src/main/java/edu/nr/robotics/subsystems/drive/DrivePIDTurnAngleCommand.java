package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.GyroCorrection;
import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;

public class DrivePIDTurnAngleCommand extends NRCommand {

	GyroCorrection gyro;
	
	public static final double TURN_PERCENTAGE = 0.5;
	
	/**
	 * Degrees in which the robot needs to be to stop DrivePIDTurnAngleCommand
	 */
	public static final Angle PID_TURN_ANGLE_THRESHOLD = new Angle(1, Angle.Unit.DEGREE);

	// TODO: PIDTurnCommand: Get p value
	public static final double P = 0.1;

	/**
	 * 
	 * @param angle
	 *            in degrees
	 */
	public DrivePIDTurnAngleCommand(Angle angle) {
		super(Drive.getInstance());
		gyro = new GyroCorrection(angle,0.3);
	}
	
	@Override
	public void onStart() {
		gyro.reset();
	}

	@Override
	public void onExecute() {
		double turn = gyro.getTurnValue(P);
		System.out.println(turn);
		Drive.getInstance().arcadeDrive(0.1, turn);
	}
	
	@Override
	public void onEnd() {
		Drive.getInstance().disable();
	}

	@Override
	public boolean isFinishedNR() {
		return gyro.getAngleError().abs().lessThan(DrivePIDTurnAngleCommand.PID_TURN_ANGLE_THRESHOLD);
	}
}
