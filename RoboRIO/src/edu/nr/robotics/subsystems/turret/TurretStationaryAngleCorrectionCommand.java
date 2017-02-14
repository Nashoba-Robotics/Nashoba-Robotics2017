package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.network.TCPServer.Num;
import edu.nr.robotics.RobotMap;

public class TurretStationaryAngleCorrectionCommand extends NRCommand{

	public TurretStationaryAngleCorrectionCommand() {
		super(Turret.getInstance());
	}
	
	@Override
	public void onExecute() {
		long angle = TCPServer.getInstance(Num.turret).getValue('a');
		long distance = TCPServer.getInstance(Num.turret).getValue('d');
		
		//Manipulates camera as if on center of robot
		double z1 = Math.sqrt(Math.pow(RobotMap.X_CAMERA_OFFSET, 2) + Math.pow(RobotMap.Y_CAMERA_OFFSET, 2));
		double theta4 = 180 - Math.atan(RobotMap.Y_CAMERA_OFFSET / RobotMap.X_CAMERA_OFFSET) - Math.atan(RobotMap.X_TURRET_OFFSET / RobotMap.Y_TURRET_OFFSET);
		double h4 = Math.sqrt(Math.pow(RobotMap.X_TURRET_OFFSET, 2) + Math.pow(RobotMap.Y_TURRET_OFFSET, 2));
		double h3 = Math.sqrt(Math.pow(h4, 2) + Math.pow(z1, 2) - 2 * h4 * z1 * Math.cos(theta4));
		double theta5 = Turret.getInstance().getPosition() * RobotMap.DEGREES_PER_ROTATION - Math.atan(RobotMap.Y_CAMERA_OFFSET / RobotMap.X_CAMERA_OFFSET);
		double theta6 = 90 - theta5 - Math.asin(h4 * Math.sin(theta4) / h3);
		double distCenter = Math.sqrt(Math.pow(distance, 2) + Math.pow(h3, 2) - 2 * distance * h3 * Math.cos(theta6 + angle));
		double angleCenter = 180 - Math.atan(RobotMap.X_TURRET_OFFSET / RobotMap.Y_TURRET_OFFSET) - z1 * Math.sin(theta4) / h3 - distance * Math.sin(theta6 + angle) / distCenter;
		
		//Manipulates camera as if on center of turret
		double theta1 = 180 - angleCenter - Math.atan(RobotMap.X_TURRET_OFFSET / RobotMap.Y_TURRET_OFFSET);
		double distReal = Math.sqrt(Math.pow(distCenter, 2) + Math.pow(h4, 2) - 2 * distCenter * h4 * Math.cos(theta1));
		double theta2 = distCenter * Math.sin(theta1) / distReal;
		double angleReal = theta2 - 90 + Math.atan(RobotMap.Y_TURRET_OFFSET / RobotMap.X_TURRET_OFFSET);
		
		angleReal /= 360; //Puts angle into rotations
		Turret.getInstance().setPosition(angleReal);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
