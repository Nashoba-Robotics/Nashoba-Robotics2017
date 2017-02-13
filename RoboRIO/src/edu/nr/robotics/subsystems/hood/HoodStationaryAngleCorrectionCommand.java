package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.network.TCPServer.Num;

public class HoodStationaryAngleCorrectionCommand extends NRCommand {

	public HoodStationaryAngleCorrectionCommand() {
		super(Hood.getInstance());
	}
	
	@Override
	public void onExecute() {
		long distance = Num.turret.getInstance().getValue('d');
		long timeStamp = Num.turret.getInstance().getValue('t');
		long currentTime = (long) (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() * 1000);
		double deltaTime = currentTime - timeStamp;
		double previousPosition = Hood.getInstance().getHistoricalPosition(deltaTime);
		double currentPosition = Hood.getInstance().getPosition();
		double deltaPosition = currentPosition - previousPosition;
		
		//TODO: Hood: Map distance to up/down angle and put function in here
		double angle = 0; //See ToDo above
		angle /= 360; //Puts angle in rotations
		angle = angle - Hood.getInstance().getHistoricalPosition(deltaTime);
		double angleToGo = angle + deltaPosition;
		Hood.getInstance().setPositionDelta(angleToGo);
		
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
}
