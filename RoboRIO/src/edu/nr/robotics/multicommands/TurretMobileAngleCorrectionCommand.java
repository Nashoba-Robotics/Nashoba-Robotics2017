package edu.nr.robotics.multicommands;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.turret.Turret;

public class TurretMobileAngleCorrectionCommand extends NRCommand{

	public TurretMobileAngleCorrectionCommand() {
		
	}
	
	@Override
	public void onExecute() {
		long timeStamp = 0; //TODO: TurretMobileAngleCorrectionCommand: Get timeStamp from data
		long histAngle = 0; //TODO: TurretMobileAngleCorrectionCommand: Get angle from data
		long histDist = 0; //TODO: TurretMobileAngleCorrectionCommand: Get distance from data
		long currentTime = (long) (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() * 1000);
		long deltaTime = currentTime - timeStamp;
		//Camera offsets are offset from 
		double yCameraOffset = 0; //TODO: TurretMobileAngleCorrectionCommand: get y camera offset
		double xCameraOffset = 0; //TODO: TurretMobileAngleCorrectionCommand: get x camera offset
		double xTurretOffset = 7;
		double yTurretOffset = 5.465;
		
		//Manipulates camera angle as if on center of robot
		double z1 = Math.sqrt(Math.pow(xCameraOffset, 2) + Math.pow(yCameraOffset, 2));
		double theta4 = 180 - Math.atan(yCameraOffset / xCameraOffset) - Math.atan(xTurretOffset / yTurretOffset);
		double h4 = Math.sqrt(Math.pow(xTurretOffset, 2) + Math.pow(yTurretOffset, 2));
		double h3 = Math.sqrt(Math.pow(h4, 2) + Math.pow(z1, 2) - 2 * h4 * z1 * Math.cos(theta4));
		double theta6 = Turret.getInstance().getHistoricalPosition(deltaTime) - Math.atan(yCameraOffset / xCameraOffset);
		double m = Math.sqrt(Math.pow(z1, 2) + Math.pow(xCameraOffset, 2) - 2 * z1 * xCameraOffset * Math.cos(theta6));
		double theta5 = Math.asin(xCameraOffset * Math.sin(theta6) / m);
		double histDistReal = Math.sqrt(Math.pow(histDist, 2) + Math.pow(h3, 2) - 2 * histDist * h3 * Math.cos(theta5 + histAngle));
		double histAngleReal = 180 - Math.atan(xTurretOffset / yTurretOffset) - z1 * Math.sin(theta4) / h3 - histDist * Math.sin(theta5 + histAngle) / histDistReal;
		
		double histRobotOrientation = histAngleReal + Turret.getInstance().getHistoricalPosition(deltaTime) * 360;
		double deltaAngle = (NavX.getInstance().getYaw(AngleUnit.DEGREE) - NavX.getInstance().getHistoricalYaw(AngleUnit.DEGREE, deltaTime));
		double curRobotOrientation = histRobotOrientation + deltaAngle;
		double histLeftPos = Drive.getInstance().getHistoricalLeftPosition(deltaTime);
		double histRightPos = Drive.getInstance().getHistoricalRightPosition(deltaTime);
		
		double turretAdd1 = -1 * (curRobotOrientation + Turret.getInstance().getPosition());
		
		//Code until next break to get current distance
		double theta1 = histRobotOrientation + 90;
		double r = Math.max(histLeftPos, histRightPos) / deltaAngle - (0.5 * RobotMap.DRIVE_WHEEL_BASE);
		double h = Math.sqrt(Math.pow(r, 2) + Math.pow(histDistReal, 2) - 2 * r * histDistReal * Math.cos(theta1));
		double theta0 = Math.asin(histDistReal * Math.sin(theta1) / h) - deltaAngle;
		double curDist = Math.sqrt(Math.pow(h, 2) + Math.pow(r, 2) - 2 * h * r * Math.cos(theta0));
		double hyp = Math.sqrt(Math.pow(xTurretOffset, 2) + Math.pow(yTurretOffset, 2));
		double theta3 = 180 - Math.atan(xTurretOffset / yTurretOffset) + curRobotOrientation;
		double curDistReal = Math.sqrt(Math.pow(curDist, 2) + Math.pow(hyp, 2) - 2 * curDist * hyp * Math.cos(theta3));
		
		double curTurretOrientation = 90 - Math.asin(curDist * Math.sin(theta3) / curDistReal) - Math.atan(yTurretOffset / xTurretOffset);
		
		//Gets average speed of two drive sides to get instantaneous speed and in (inches / sec)
		double speed = (Drive.getInstance().getEncoderLeftSpeed() + Drive.getInstance().getEncoderRightSpeed()) / 2 * RobotMap.DRIVE_WHEEL_DIAMETER / 60;
		
		//Code until next break gets additional angle for turret to turn based on current speed
		double timeUntilMake = 0; //TODO: TurretMobileAngleCorrectionCommand: Map turret distance to ball time in air and add time it takes for turret to move to spot and ball to shoot
		double p = speed * (timeUntilMake);
		double e = Math.sqrt(Math.pow(curDistReal, 2) + Math.pow(p, 2) - 2 * curDistReal * p * Math.cos(curTurretOrientation));
		double turretAdd2 = 180 - Math.asin(curDistReal * Math.sin(curTurretOrientation) / e) - curTurretOrientation;
		
		double previousPosition = Turret.getInstance().getHistoricalPosition(deltaTime);
		double currentPosition = Turret.getInstance().getPosition();
		double deltaPosition = currentPosition - previousPosition;
		double turretAdd3 = histAngleReal + deltaPosition; //In rotations
		
		double turretAdd = turretAdd1 + turretAdd2 + turretAdd3;
		turretAdd /= 360; // Changes turret add to rotations instead of angle
		Turret.getInstance().setPositionDelta(turretAdd);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
