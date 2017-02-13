package edu.nr.robotics.subsystems.intakeArm;

import edu.nr.lib.commandbased.DoNothingJoystickCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.intake.Intake;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class IntakeArm extends NRSubsystem {
	public static IntakeArm singleton;

	private DoubleSolenoid intakeArm;

	public enum IntakeArmState {
		DEPLOYED, RETRACTED
	}

	/**
	 * The current state of the intake arm, either retracted or deployed.
	 * 
	 * The default is retracted, since that is how the robot will start.
	 */
	public IntakeArmState currentIntakeArmState = IntakeArmState.RETRACTED;

	private IntakeArm() {
		if (EnabledSubsystems.INTAKE_ARM_ENABLED) {
			intakeArm = new DoubleSolenoid(RobotMap.INTAKE_ARM_PNEUMATIC, RobotMap.INTAKE_ARM_FORWARD,
					RobotMap.INTAKE_ARM_REVERSE);
			// TODO: IntakeArm: Check solenoid for current state

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
		currentIntakeArmState = IntakeArmState.DEPLOYED;
	}

	public void retractIntakeArm() {

		Intake.getInstance().onIntakeArmRetract();

		intakeArm.set(Value.kReverse);
		currentIntakeArmState = IntakeArmState.RETRACTED;
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

	public boolean intakeArmIsDeployed() {
		return currentIntakeArmState == IntakeArmState.DEPLOYED;
	}

	public boolean intakeArmIsRetracted() {
		return currentIntakeArmState == IntakeArmState.RETRACTED;
	}

}