package edu.nr.robotics.subsystems.climber;

import edu.nr.lib.NRSubsystem;

public class Climber extends NRSubsystem {

	public static Climber singleton;

	public static Climber getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new Climber();
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
