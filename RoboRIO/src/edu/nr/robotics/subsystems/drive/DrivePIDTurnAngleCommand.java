package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.RobotMap;

public class DrivePIDTurnAngleCommand extends NRCommand {
	
	double angle = 0; //Angle to turn in degrees
	
	double initialAngle = 0;
	double finishAngle = 0;
	
	//TODO: Get p value for PIDTurnCommand
	public static final double P = 0;
	
	public DrivePIDTurnAngleCommand(double angle) {
		super(Drive.getInstance());
		this.angle = angle;
	}
	
	@Override
	public void onStart() {
		initialAngle = NavX.getInstance().getYaw(AngleUnit.DEGREE);
		finishAngle = initialAngle + angle;
	}
	
	@Override
	public void onExecute() {
		Drive.getInstance().arcadeDrive(0, P * (finishAngle - NavX.getInstance().getYaw(AngleUnit.DEGREE)) / (finishAngle - initialAngle));
	}
	
	@Override
	public boolean isFinishedNR() {
		return Math.abs(finishAngle - NavX.getInstance().getYaw(AngleUnit.DEGREE)) < RobotMap.DRIVE_PID_TURN_ANGLE_THRESHOLD;
	}
}
