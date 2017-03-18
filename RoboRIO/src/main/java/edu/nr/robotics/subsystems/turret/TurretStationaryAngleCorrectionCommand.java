package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.StationaryTrackingCalculation;

public class TurretStationaryAngleCorrectionCommand extends NRCommand{

		
	public TurretStationaryAngleCorrectionCommand() {
		super(Turret.getInstance());
	}
	
	@Override
	public void onStart() {
		Turret.getInstance().setAutoAlign(true);
	}
	
	Time timeOfStoppedMoving = Time.ZERO;
	
	Time cameraWaitTime = new Time(250, Time.Unit.MILLISECOND);
	
	Angle threshold = new Angle(1, Angle.Unit.DEGREE);
	
	@Override
	public void onExecute() {
		if(Turret.getInstance().getSpeed().equals(AngularSpeed.ZERO) && timeOfStoppedMoving.equals(Time.ZERO)) {
			timeOfStoppedMoving = Time.getCurrentTime();
		} else {
			timeOfStoppedMoving = Time.ZERO;
		}
		if(StationaryTrackingCalculation.getInstance().canSeeTarget()) {
			if(StationaryTrackingCalculation.getInstance().getDeltaTurretAngle().abs().greaterThan(threshold)) {
				if(timeOfStoppedMoving.greaterThan(cameraWaitTime)) {
					//System.out.println("Not Aligned: " + StationaryTrackingCalculation.getInstance().getDeltaTurretAngle().get(Angle.Unit.DEGREE));
					//System.out.println("Angle Turned To: " + (StationaryTrackingCalculation.getInstance().getTurretAngle().sub(Turret.getInstance().getPosition()).get(Angle.Unit.DEGREE)));
					Turret.getInstance().setPosition(StationaryTrackingCalculation.getInstance().getTurretAngle());			
				}
			}
		}
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}
	
	@Override
	public void onEnd() {
		Turret.getInstance().setAutoAlign(false);
	}
}
