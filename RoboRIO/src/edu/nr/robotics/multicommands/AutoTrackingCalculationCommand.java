package edu.nr.robotics.multicommands;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.network.TCPServer;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.turret.Turret;

public class AutoTrackingCalculationCommand extends NRCommand {
	
	static double turretAngle = 0;
	static double hoodAngle = 0;
	static double shooterSpeed = 0;
	
	public AutoTrackingCalculationCommand() {
		super();
	}
	
	@Override
	public void onExecute() {
		if (Shooter.getInstance().isAutoAlign() || Turret.getInstance().isAutoAlign() || Hood.getInstance().isAutoAlign()) {
			long timeStamp = TCPServer.Num.turret.getInstance().getValue('t');
			long histAngle = TCPServer.Num.turret.getInstance().getValue('a');
			long histDist = TCPServer.Num.turret.getInstance().getValue('d');
			long currentTime = (long) (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() * RobotMap.MILLISECONDS_PER_SECOND);
			long deltaTime = currentTime - timeStamp;
			
			//Code until break manipulates camera angle as if on center of robot
			double z1 = Math.sqrt(Math.pow(RobotMap.X_CAMERA_OFFSET, 2) + Math.pow(RobotMap.Y_CAMERA_OFFSET, 2));
			double theta4 = 180 - Math.atan(RobotMap.Y_CAMERA_OFFSET / RobotMap.X_CAMERA_OFFSET) - Math.atan(RobotMap.X_TURRET_OFFSET / RobotMap.Y_TURRET_OFFSET);
			double h4 = Math.sqrt(Math.pow(RobotMap.X_TURRET_OFFSET, 2) + Math.pow(RobotMap.Y_TURRET_OFFSET, 2));
			double h3 = Math.sqrt(Math.pow(h4, 2) + Math.pow(z1, 2) - 2 * h4 * z1 * Math.cos(theta4));
			double theta5 = Turret.getInstance().getHistoricalPosition(deltaTime) * RobotMap.DEGREES_PER_ROTATION - Math.atan(RobotMap.Y_CAMERA_OFFSET / RobotMap.X_CAMERA_OFFSET);
			double theta6 = 90 - theta5 - Math.asin(h4 * Math.sin(theta4) / h3);
			double histDistCenter = Math.sqrt(Math.pow(histDist, 2) + Math.pow(h3, 2) - 2 * histDist * h3 * Math.cos(theta6 + histAngle));
			double histAngleCenter = 180 - Math.atan(RobotMap.X_TURRET_OFFSET / RobotMap.Y_TURRET_OFFSET) - z1 * Math.sin(theta4) / h3 - histDist * Math.sin(theta6 + histAngle) / histDistCenter;
			
			double histRobotOrientation = histAngleCenter + Turret.getInstance().getHistoricalPosition(deltaTime) * RobotMap.DEGREES_PER_ROTATION;
			double deltaAngle = (NavX.getInstance().getYaw(AngleUnit.DEGREE) - NavX.getInstance().getHistoricalYaw(AngleUnit.DEGREE, deltaTime));
			double curRobotOrientation = histRobotOrientation + deltaAngle;
			double histLeftPos = Drive.getInstance().getHistoricalLeftPosition(deltaTime) * RobotMap.DEGREES_PER_ROTATION;
			double histRightPos = Drive.getInstance().getHistoricalRightPosition(deltaTime) * RobotMap.DEGREES_PER_ROTATION;
			
			//Code until next break to get current distance and turret orientation
			double theta1 = histRobotOrientation + 90;
			double r = Math.max(histLeftPos, histRightPos) / deltaAngle - (0.5 * RobotMap.DRIVE_WHEEL_BASE);
			double h = Math.sqrt(Math.pow(r, 2) + Math.pow(histDistCenter, 2) - 2 * r * histDistCenter * Math.cos(theta1));
			double theta0 = Math.asin(histDistCenter * Math.sin(theta1) / h) - deltaAngle;
			double curDist = Math.sqrt(Math.pow(h, 2) + Math.pow(r, 2) - 2 * h * r * Math.cos(theta0));
			double hyp = Math.sqrt(Math.pow(RobotMap.X_TURRET_OFFSET, 2) + Math.pow(RobotMap.Y_TURRET_OFFSET, 2));
			double theta3 = 180 - Math.atan(RobotMap.X_TURRET_OFFSET / RobotMap.Y_TURRET_OFFSET) + curRobotOrientation;
			double curDistReal = Math.sqrt(Math.pow(curDist, 2) + Math.pow(hyp, 2) - 2 * curDist * hyp * Math.cos(theta3));
			double curTurretOrientation = 90 - Math.asin(curDist * Math.sin(theta3) / curDistReal) - Math.atan(RobotMap.Y_TURRET_OFFSET / RobotMap.X_TURRET_OFFSET);
			
			//Gets average speed of two drive sides to get instantaneous speed in (inches / sec)
			double speed = (Drive.getInstance().getEncoderLeftSpeed() + Drive.getInstance().getEncoderRightSpeed()) / 2 * (RobotMap.DRIVE_WHEEL_DIAMETER * RobotMap.INCHES_PER_FOOT) / RobotMap.SECONDS_PER_MINUTE;
			double vertSpeed = speed * Math.cos(curRobotOrientation);
			
			//Code until next break gets additional angle for turret to turn based on current speed
			double timeUntilMake = 0; //TODO: Turret: Map turret distance to ball time in air and add time it takes for turret to move to spot and ball to shoot
			double p = speed * (timeUntilMake);
			double e = Math.sqrt(Math.pow(curDistReal, 2) + Math.pow(p, 2) - 2 * curDistReal * p * Math.cos(curTurretOrientation));
			double turretAngle = 180 - Math.asin(curDistReal * Math.sin(curTurretOrientation) / e);
			//Sets the change in position of the turret
			turretAngle /= RobotMap.DEGREES_PER_ROTATION; // Changes from angle to rotations
			
			//What the distance of the shot will map as due to forward/backward motion
			double feltDist = vertSpeed * timeUntilMake;
			//TODO: Hood: Map feltDist to hood angle
			this.hoodAngle = 0;
			//Sets the position of the hood
			hoodAngle /= RobotMap.DEGREES_PER_ROTATION; // Changes from angle to rotations
			
			//TODO: Shooter: Map feltDist to shooter speed in rpm
			this.shooterSpeed = 0;
		}
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
	public static double getTurretAngle() {
		return turretAngle;
	}

	public static double getHoodAngle() {
		return hoodAngle;
	}
	
	public static double getShooterSpeed() {
		return shooterSpeed;
	}
}
