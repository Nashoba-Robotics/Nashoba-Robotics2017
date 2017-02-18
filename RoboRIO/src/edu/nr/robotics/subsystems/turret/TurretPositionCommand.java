package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.RobotMap;

public class TurretPositionCommand extends NRCommand{

	double position; //In rotations
	
	public TurretPositionCommand(double position) {
		super(Turret.getInstance());
		this.position = position;
	}

	@Override
	public void onStart() {
		Turret.getInstance().setPosition(position);
	}
	
	@Override
	public boolean isFinishedNR() {
		return Turret.getInstance().getPosition() < position + RobotMap.TURRET_POSITION_THRESHOLD && Turret.getInstance().getPosition() > position - RobotMap.TURRET_POSITION_THRESHOLD;
	}
}
