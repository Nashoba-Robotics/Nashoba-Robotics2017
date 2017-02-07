package edu.nr.robotics.subsystems.intakeArm;

import edu.nr.lib.commandbased.DoNothingJoystickCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class IntakeArm extends NRSubsystem {
	public static IntakeArm singleton;

	private DoubleSolenoid intakeArm;

	private IntakeArm() {
		if (EnabledSubsystems.INTAKE_ARM_ENABLED) {
			intakeArm = new DoubleSolenoid(RobotMap.INTAKE_ARM_PNEUMATIC, RobotMap.INTAKE_ARM_FORWARD,
					RobotMap.INTAKE_ARM_REVERSE);
		}
	}

	public static void init() {
		if (singleton == null) {
			singleton = new IntakeArm();
			singleton.setJoystickCommand(new DoNothingJoystickCommand(singleton));
		}
	}

	public void deployIntakeArm() {
		intakeArm.set(Value.kForward);
	}

	public void retractIntakeArm() {
		intakeArm.set(Value.kReverse);
	}

	public static IntakeArm getInstance() {
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
		intakeArm.set(Value.kOff);
	}

}