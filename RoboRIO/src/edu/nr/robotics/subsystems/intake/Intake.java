package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.NRSubsystem;

public class Intake extends NRSubsystem {

	public static Intake singleton;

	public static Intake getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new Intake();
			//TODO getInstance().setJoystickCommand();
		}
	}
	
	@Override
	public void disable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void smartDashboardInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void periodic() {
		// TODO Auto-generated method stub
		
	}

}
