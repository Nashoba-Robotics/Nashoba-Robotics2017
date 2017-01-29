package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.NRSubsystem;

public class Turret extends NRSubsystem {

	public static Turret singleton;

	public static Turret getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new Turret();
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
