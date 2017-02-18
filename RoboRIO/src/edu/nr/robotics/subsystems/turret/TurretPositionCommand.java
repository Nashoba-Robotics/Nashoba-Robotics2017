package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class TurretPositionCommand extends NRCommand{

	double position; //In degrees
	
	/**
	 * @param position in degrees
	 */
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
		return Turret.getInstance().getPosition() < position + Turret.POSITION_THRESHOLD && Turret.getInstance().getPosition() > position - Turret.POSITION_THRESHOLD;
	}
}
