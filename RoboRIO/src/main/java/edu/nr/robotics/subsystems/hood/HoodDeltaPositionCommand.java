package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class HoodDeltaPositionCommand extends NRCommand{

	Angle deltaPosition;
	Angle goalPosition;
	
	/**
	 * @param deltaPosition
	 */
	public HoodDeltaPositionCommand(Angle deltaPosition) {
		super(Hood.getInstance());
		this.deltaPosition = deltaPosition;
	}
	
	@Override
	public void onStart() {
		goalPosition = Hood.getInstance().getPosition().add(deltaPosition);
		Hood.getInstance().setPosition(goalPosition);
	}
	
	@Override
	public boolean isFinishedNR() {
		return Hood.getInstance().getPosition().lessThan(goalPosition.add(Hood.POSITION_THRESHOLD)) && Hood.getInstance().getPosition().lessThan(goalPosition.sub(Hood.POSITION_THRESHOLD));
	}
}
