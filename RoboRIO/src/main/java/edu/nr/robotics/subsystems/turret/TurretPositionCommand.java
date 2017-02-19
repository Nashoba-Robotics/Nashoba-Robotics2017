package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Type;

public class TurretPositionCommand extends NRCommand {

	Angle position;

	/**
	 * @param position
	 */
	public TurretPositionCommand(Angle position) {
		super(Turret.getInstance());
		this.position = position;
	}

	@Override
	public void onStart() {
		Turret.getInstance().setPosition(position);
	}

	@Override
	public boolean isFinishedNR() {
		return Turret.getInstance().getPosition().get(Type.DEGREE) < 
				position.add(Turret.POSITION_THRESHOLD).get(Type.DEGREE)
				&& 
				Turret.getInstance().getPosition().get(Type.ROTATION) > 
				position.sub(Turret.POSITION_THRESHOLD).get(Type.ROTATION);
	}
}
