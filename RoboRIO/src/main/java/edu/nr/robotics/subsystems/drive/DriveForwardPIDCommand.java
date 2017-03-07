package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveForwardPIDCommand extends NRCommand {

	Distance distance;
	Distance startingPositionLeft;
	Distance startingPositionRight;
	
	Distance leftPositionAccumulator = Distance.ZERO;
	Distance rightPositionAccumulator = Distance.ZERO;
	
	Distance lastErrorLeft;
	Distance lastErrorRight;
	Time lastTime;

	double KP = 0;
	double KI = 0;
	double KD = 0;

	/**
	 * Drive forward
	 * 
	 * @param distance
	 */
	public DriveForwardPIDCommand(Distance distance) {
		super(Drive.getInstance());
		this.distance = distance;
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
		
		lastTime = Time.getCurrentTime();
	}
	
	@Override
	public void onExecute() {
		Time time = Time.getCurrentTime();
		Time dt = time.sub(lastTime);
		
		Distance leftError = startingPositionLeft.add(distance).sub(Drive.getInstance().getLeftPosition());
		Distance dLeftError = leftError.sub(lastErrorLeft);
		
		double leftOutput = KP * leftError.get(Distance.Unit.DRIVE_ROTATION) + KD * dLeftError.get(Distance.Unit.DRIVE_ROTATION) / dt.get(Time.Unit.SECOND) + KI * leftPositionAccumulator.get(Distance.Unit.DRIVE_ROTATION);

		Distance rightError = startingPositionRight.add(distance).sub(Drive.getInstance().getRightPosition());
		Distance dRightError = rightError.sub(lastErrorRight);

		double rightOutput = KP * rightError.get(Distance.Unit.DRIVE_ROTATION) + KD * dRightError.get(Distance.Unit.DRIVE_ROTATION) / dt.get(Time.Unit.SECOND) + KI * rightPositionAccumulator.get(Distance.Unit.DRIVE_ROTATION);

		Drive.getInstance().setMotorSpeedInPercent(leftOutput, rightOutput);
		
		lastErrorLeft = leftError;
		lastErrorRight = rightError;
		lastTime = time;
		leftPositionAccumulator = leftPositionAccumulator.add(leftError);
		rightPositionAccumulator = rightPositionAccumulator.add(rightError);
	}

	@Override
	public boolean isFinishedNR() {
		Distance leftError = startingPositionLeft.add(distance).sub(Drive.getInstance().getLeftPosition());
		Distance rightError = startingPositionRight.add(distance).sub(Drive.getInstance().getRightPosition());
		
		return leftError.abs().lessThan(Drive.PROFILE_POSITION_THRESHOLD) && rightError.abs().lessThan(Drive.PROFILE_POSITION_THRESHOLD);
	}
}
