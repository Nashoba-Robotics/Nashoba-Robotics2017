package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.turret.Turret;

public class HoodPositionCommand extends NRCommand{

	double position;
	
	public HoodPositionCommand(double position) {
		super(Hood.getInstance());
		this.position = position;
	}

	@Override
	public void onStart() {
		Hood.getInstance().setPosition(position);
	}
	
	@Override
	public boolean isFinishedNR() {
		return Hood.getInstance().getPosition() < position + RobotMap.HOOD_POSITION_THRESHOLD && Hood.getInstance().getPosition() > position - RobotMap.HOOD_POSITION_THRESHOLD;
	}

}
