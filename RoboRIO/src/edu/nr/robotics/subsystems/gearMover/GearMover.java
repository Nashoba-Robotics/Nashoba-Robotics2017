package edu.nr.robotics.subsystems.gearMover;

import edu.nr.lib.commandbased.DoNothingJoystickCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class GearMover extends NRSubsystem {
	public static GearMover singleton;
	
	public DoubleSolenoid GearMover;
	
	private GearMover() {
		if (EnabledSubsystems.GEAR_MOVER_ENABLED) {
			GearMover = new DoubleSolenoid(RobotMap.GEAR_MOVER_PNEUMATIC, RobotMap.GEAR_MOVER_FORWARD,
					RobotMap.GEAR_MOVER_REVERSE);
		}
	}

	public static void init() {
		if (singleton == null) {
			singleton = new GearMover();
			singleton.setJoystickCommand(new DoNothingJoystickCommand(singleton));
		}
	}

	public void deployGearMover() {
		GearMover.set(Value.kForward);
	}

	public void retractGearMover() {
		GearMover.set(Value.kReverse);
	}

	public static GearMover getInstance() {
		init();
		return singleton;
	}

	@Override
	public void smartDashboardInfo() {

	}

	@Override
	public void periodic() {

	}

	@Override
	public void disable() {
		GearMover.set(Value.kOff);
	}

}
