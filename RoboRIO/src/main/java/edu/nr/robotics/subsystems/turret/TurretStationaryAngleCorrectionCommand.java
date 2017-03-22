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
	
	Time cameraWaitTime = new Time(500, Time.Unit.MILLISECOND);
	
	Angle threshold = new Angle(0.3, Angle.Unit.DEGREE);
	
	@Override
	public void onExecute() {
		System.out.println("Current time: " + Time.getCurrentTime().sub(timeOfStoppedMoving).get(Time.Unit.SECOND));
		if(Turret.getInstance().getSpeed().equals(AngularSpeed.ZERO) && timeOfStoppedMoving.equals(Time.ZERO)) {
			timeOfStoppedMoving = Time.getCurrentTime();
			System.out.println("Not moving");
		} else {
			if(!Turret.getInstance().getSpeed().equals(AngularSpeed.ZERO)) {
				timeOfStoppedMoving = Time.ZERO;
				System.out.println("Resetting time");
			}
		}
		if(Turret.getInstance().getSpeed().equals(AngularSpeed.ZERO)) {
			System.out.println("Not moving");
			if(StationaryTrackingCalculation.getInstance().canSeeTarget()) {
				System.out.println("Can see");
				if(StationaryTrackingCalculation.getInstance().getDeltaTurretAngle().abs().greaterThan(threshold)) {
					System.out.println("Outside of threshold");
					if(Time.getCurrentTime().sub(timeOfStoppedMoving).greaterThan(cameraWaitTime)) {
						//System.out.println("Not Aligned: " + StationaryTrackingCalculation.getInstance().getDeltaTurretAngle().get(Angle.Unit.DEGREE));
						System.out.println("Angle Turning: " + (StationaryTrackingCalculation.getInstance().getTurretAngle().get(Angle.Unit.DEGREE)));
						Turret.getInstance().setPosition(StationaryTrackingCalculation.getInstance().getTurretAngle());			
					}
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
