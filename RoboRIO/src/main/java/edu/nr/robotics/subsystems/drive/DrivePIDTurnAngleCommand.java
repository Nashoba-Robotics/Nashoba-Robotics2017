package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.GyroCorrection;
import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivePIDTurnAngleCommand extends NRCommand {

	GyroCorrection gyro;
	
	public static final double START_TURN_THRESHOLD = 0.1;
	
	/**
	 * Degrees in which the robot needs to be to stop DrivePIDTurnAngleCommand
	 */
	public static final Angle PID_TURN_ANGLE_THRESHOLD = new Angle(0.3, Angle.Unit.DEGREE);

	public static double P = 0.01;

	boolean setAngle;
	
	/**
	 * 
	 * @param angle
	 *            in degrees
	 */
	public DrivePIDTurnAngleCommand() {
		super(Drive.getInstance());
		
		this.setAngle = false;
	}
	
	/**
	 * 
	 * @param angle
	 *            in degrees
	 */
	public DrivePIDTurnAngleCommand(Angle angle) {
		super(Drive.getInstance());
		
		this.setAngle = true;
		
		gyro = new GyroCorrection(angle,0.3);

	}
	
	@Override
	public void onStart() {
		if(!setAngle) {
			SmartDashboard.putNumber("Turn angle", SmartDashboard.getNumber("Turn angle", 0));
			Angle angle = new Angle(SmartDashboard.getNumber("Turn angle", 0), Angle.Unit.DEGREE);
	
			gyro = new GyroCorrection(angle,0.3);
			gyro.reset();
		} else {
			gyro.reset();
		}
		SmartDashboard.putNumber("P Value", SmartDashboard.getNumber("P Value", 0));
		P = SmartDashboard.getNumber("P Value", 0);
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
		return gyro.getAngleError().abs().lessThan(DrivePIDTurnAngleCommand.PID_TURN_ANGLE_THRESHOLD);
	}
}
