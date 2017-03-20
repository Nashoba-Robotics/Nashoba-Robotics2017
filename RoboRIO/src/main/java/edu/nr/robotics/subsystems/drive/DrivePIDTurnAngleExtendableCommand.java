package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.GyroCorrection;
import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class DrivePIDTurnAngleExtendableCommand extends NRCommand {

	GyroCorrection gyro;
	
	public static final double START_TURN_THRESHOLD = 0.06;
	
	/**
	 * Degrees in which the robot needs to be to stop DrivePIDTurnAngleCommand
	 */
	public static final Angle PID_TURN_ANGLE_THRESHOLD = new Angle(0.3, Angle.Unit.DEGREE);

	public static double P = 0.01;

	
	
	Angle angle;
	
	public abstract Angle getAngleToTurn();
	
	/**
	 * 
	 * @param angle
	 *            in degrees
	 */
	public DrivePIDTurnAngleExtendableCommand() {
		super(Drive.getInstance());
	}
	
	@Override
	public void onStart() {
		angle = getAngleToTurn();
		gyro = new GyroCorrection(angle,0.3);
		gyro.reset();
	}

	@Override
	public void onExecute() {
		double turn = gyro.getTurnValue(P);
		if (Math.abs(turn) < START_TURN_THRESHOLD) {
			turn = START_TURN_THRESHOLD * Math.signum(turn);
		}
		SmartDashboard.putNumber("Turn value", turn);
		Drive.getInstance().arcadeDrive(-START_TURN_THRESHOLD, turn);
	}
	
	@Override
	public void onEnd() {
		Drive.getInstance().disable();
	}

	@Override
	public boolean isFinishedNR() {
		return gyro.getAngleError().abs().lessThan(DrivePIDTurnAngleExtendableCommand.PID_TURN_ANGLE_THRESHOLD);
	}
}
