package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.network.TCPServer;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.turret.Turret;

public class HoodMobileAngleCorrectionCommand extends NRCommand {

	public HoodMobileAngleCorrectionCommand() {
		super(Hood.getInstance());
	}
	
	@Override
	public void onExecute() {
		long timeStamp = TCPServer.getInstance(TCPServer.Num.turret).getValue('t');
		long histAngle = TCPServer.getInstance(TCPServer.Num.turret).getValue('a');
		long histDist = TCPServer.getInstance(TCPServer.Num.turret).getValue('d');
		long currentTime = (long) (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() * 1000);
		long deltaTime = currentTime - timeStamp;
		
		double yCameraOffset = RobotMap.Y_CAMERA_OFFSET;
		double xCameraOffset = RobotMap.X_CAMERA_OFFSET;
		double xTurretOffset = RobotMap.X_TURRET_OFFSET;
		double yTurretOffset = RobotMap.Y_TURRET_OFFSET;
		
		//Manipulates camera angle as if on center of robot
		double z1 = Math.sqrt(Math.pow(xCameraOffset, 2) + Math.pow(yCameraOffset, 2));
		double theta4 = 180 - Math.atan(yCameraOffset / xCameraOffset) - Math.atan(xTurretOffset / yTurretOffset);
		double h4 = Math.sqrt(Math.pow(xTurretOffset, 2) + Math.pow(yTurretOffset, 2));
		double h3 = Math.sqrt(Math.pow(h4, 2) + Math.pow(z1, 2) - 2 * h4 * z1 * Math.cos(theta4));
		double theta5 = Turret.getInstance().getHistoricalPosition(deltaTime) * 360 - Math.atan(yCameraOffset / xCameraOffset);
		double theta6 = 90 - theta5 - Math.asin(h4 * Math.sin(theta4) / h3);
		double histDistReal = Math.sqrt(Math.pow(histDist, 2) + Math.pow(h3, 2) - 2 * histDist * h3 * Math.cos(theta6 + histAngle));
		double histAngleReal = 180 - Math.atan(xTurretOffset / yTurretOffset) - z1 * Math.sin(theta4) / h3 - histDist * Math.sin(theta6 + histAngle) / histDistReal;
		
		double histRobotOrientation = histAngleReal + Turret.getInstance().getHistoricalPosition(deltaTime) * 360;
		double deltaAngle = (NavX.getInstance().getYaw(AngleUnit.DEGREE) - NavX.getInstance().getHistoricalYaw(AngleUnit.DEGREE, deltaTime));
		double curRobotOrientation = histRobotOrientation + deltaAngle;
		double histLeftPos = Drive.getInstance().getHistoricalLeftPosition(deltaTime) * 360;
		double histRightPos = Drive.getInstance().getHistoricalRightPosition(deltaTime) * 360;
		
		//Code until next break to get current distance
		double theta1 = histRobotOrientation + 90;
		double r = Math.max(histLeftPos, histRightPos) / deltaAngle - (0.5 * RobotMap.DRIVE_WHEEL_BASE);
		double h = Math.sqrt(Math.pow(r, 2) + Math.pow(histDistReal, 2) - 2 * r * histDistReal * Math.cos(theta1));
		double theta0 = Math.asin(histDistReal * Math.sin(theta1) / h) - deltaAngle;
		double curDist = Math.sqrt(Math.pow(h, 2) + Math.pow(r, 2) - 2 * h * r * Math.cos(theta0));
		double hyp = Math.sqrt(Math.pow(xTurretOffset, 2) + Math.pow(yTurretOffset, 2));
		double theta3 = 180 - Math.atan(xTurretOffset / yTurretOffset) + curRobotOrientation;
		double curDistReal = Math.sqrt(Math.pow(curDist, 2) + Math.pow(hyp, 2) - 2 * curDist * hyp * Math.cos(theta3));
	
		double hoodAdd1 = -Hood.getInstance().getPosition() * 360;
		
		//Gets average speed of two drive sides to get instantaneous speed in (inches / sec)
		double speed = (Drive.getInstance().getEncoderLeftSpeed() + Drive.getInstance().getEncoderRightSpeed()) / 2 * (RobotMap.DRIVE_WHEEL_DIAMETER * RobotMap.INCHES_PER_FOOT) / RobotMap.SECONDS_PER_MINUTE;
		double vertSpeed = Math.cos(speed);
		double timeUntilMake = 0;
		//What the distance of the shot will map as due to forward/backward motion
		double feltDist = vertSpeed * timeUntilMake;
		//TODO: HoodMobileAngleCorrectionCommand: Map feltDist to hood angle
		double hoodAdd2 = 0;
		
		//Code until next break gets additional angle for turret to turn based on turret movement
		double previousPosition = Hood.getInstance().getHistoricalPosition(deltaTime) * 360;
		double currentPosition = Hood.getInstance().getPosition() * 360;
		double deltaPosition = currentPosition - previousPosition;
		double hoodAdd3 = histAngleReal + deltaPosition;
		
		//Sets the change in position of the turret
		double hoodAdd = hoodAdd1 + hoodAdd2 + hoodAdd3;
		hoodAdd /= 360; // Changes from angle to rotations
		Hood.getInstance().setPositionDelta(hoodAdd);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
