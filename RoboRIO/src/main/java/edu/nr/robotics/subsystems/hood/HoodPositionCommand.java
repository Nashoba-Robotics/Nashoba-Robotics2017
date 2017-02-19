package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class HoodPositionCommand extends NRCommand {

	Angle position;

	/**
	 * @param position
	 *            Degrees
	 */
	public HoodPositionCommand(Angle position) {
		super(Hood.getInstance());
		this.position = position;
	}

	@Override
	public void onStart() {
		Hood.getInstance().setPosition(position);
	}

	@Override
	public boolean isFinishedNR() {
		return Hood.getInstance().getPosition().lessThan(position.add(Hood.POSITION_THRESHOLD))
				&& Hood.getInstance().getPosition().greaterThan(position.add(Hood.POSITION_THRESHOLD));
	}

}
