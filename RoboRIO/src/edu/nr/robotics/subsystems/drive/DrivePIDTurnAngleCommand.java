package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.RobotMap;

public class DrivePIDTurnAngleCommand extends NRCommand {
	
	double angle = 0; //Angle to turn in degrees
	
	double initialAngle = 0;
	double finishAngle = 0;
	
	//TODO: PIDTurnCommand: Get p value 
	public static final double P = 0;
	
	/**
	 * 
	 * @param angle in degrees
	 */
	public DrivePIDTurnAngleCommand(double angle) {
		super(Drive.getInstance());
		this.angle = angle;
	}
	
	@Override
	public void onStart() {
		initialAngle = NavX.getInstance().getYaw(AngleUnit.DEGREE);
		finishAngle = initialAngle + angle;
	}
	
	private double getAngleError() {
		return finishAngle - NavX.getInstance().getYaw(AngleUnit.DEGREE);
	}
	
	@Override
	public void onExecute() {
		double turn = P * getAngleError();
		Drive.getInstance().arcadeDrive(0, turn);
	}
	
	@Override
	public boolean isFinishedNR() {
		return Math.abs(getAngleError()) < RobotMap.DRIVE_PID_TURN_ANGLE_THRESHOLD;
	}
}
