package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSmartDashboardSpeedCommand extends NRCommand {

	AngularSpeed speed;
	
	public ShooterSmartDashboardSpeedCommand() {
		super(Shooter.getInstance());
	}
	
	@Override
	public void onStart() {
		SmartDashboard.putNumber("Shooter speed to run at", SmartDashboard.getNumber("Shooter speed to run at", 0));
		speed = new AngularSpeed(SmartDashboard.getNumber("Shooter speed to run at", 0), Angle.Unit.ROTATION, Time.Unit.MINUTE);
		Shooter.getInstance().setMotorSpeedInRPM(speed);
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}

}
