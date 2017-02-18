package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.Units;
import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveForwardPIDCommand extends NRCommand {

	double distance; // Rotations
	double startingPositionLeft; //Rotations
	double startingPositionRight; //Rotations
	
	double leftPositionAccumulator = 0;
	double rightPositionAccumulator = 0;
	
	double lastErrorLeft;
	double lastErrorRight;
	double lastTime;

	double KP = 0;
	double KI = 0;
	double KD = 0;

	/**
	 * Drive forward
	 * 
	 * @param distance
	 *            The distance in inches
	 */
	public DriveForwardPIDCommand(double distance) {
		super(Drive.getInstance());
		this.distance = distance / Units.INCHES_PER_FOOT / Drive.DISTANCE_PER_REV;
	}
	
	@Override
	public void onStart() {
		startingPositionLeft = Drive.getInstance().getLeftPosition();
		startingPositionRight = Drive.getInstance().getRightPosition();
		
		lastErrorLeft = distance;
		lastErrorRight = distance;
		
		SmartDashboard.putNumber("Drive Forward PID P", SmartDashboard.getNumber("Drive Forward PID P", 0));
		SmartDashboard.putNumber("Drive Forward PID I", SmartDashboard.getNumber("Drive Forward PID I", 0));
		SmartDashboard.putNumber("Drive Forward PID D", SmartDashboard.getNumber("Drive Forward PID D", 0));
		
		KP = SmartDashboard.getNumber("Drive Forward PID P", 0);
		KI = SmartDashboard.getNumber("Drive Forward PID I", 0);
		KD = SmartDashboard.getNumber("Drive Forward PID D", 0);
	}
	
	@Override
	public void onExecute() {
		double time = Timer.getFPGATimestamp();
		double dt = time - lastTime;
		
		double leftError = startingPositionLeft + distance - Drive.getInstance().getLeftPosition();
		double dLeftError = leftError - lastErrorLeft;
		
		double leftOutput = KP * leftError + KD * dLeftError / dt + KI * leftPositionAccumulator;

		double rightError = startingPositionRight + distance - Drive.getInstance().getRightPosition();
		double dRightError = rightError - lastErrorRight;

		double rightOutput = KP * rightError + KD * dRightError / dt + KI * rightPositionAccumulator;

		Drive.getInstance().setMotorSpeedInPercent(leftOutput, rightOutput);
		
		lastErrorLeft = leftError;
		lastErrorRight = rightError;
		lastTime = time;
		leftPositionAccumulator += leftError;
		rightPositionAccumulator += rightError;
	}

	@Override
	public boolean isFinishedNR() {
		double leftError = startingPositionLeft + distance - Drive.getInstance().getLeftPosition();
		double rightError = startingPositionRight + distance - Drive.getInstance().getRightPosition();
		
		return Math.abs(leftError) < Drive.PROFILE_POSITION_THRESHOLD && Math.abs(rightError) < Drive.PROFILE_POSITION_THRESHOLD;
	}
}
