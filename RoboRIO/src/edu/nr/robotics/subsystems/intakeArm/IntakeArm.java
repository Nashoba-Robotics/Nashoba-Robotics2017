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

	//TODO: IntakeArm: determine direction
	
	public enum IntakeArmState {
		DEPLOYED, RETRACTED
	}
	
	private static Value DEPLOYED_VALUE = Value.kForward;
	private static Value RETRACTED_VALUE = Value.kForward;

	/**
	 * The current state of the intake arm, either retracted or deployed.
	 * 
	 * The default is retracted, since that is how the robot will start.
	 */
	public IntakeArmState currentIntakeArmState;

	private IntakeArm() {
		if (EnabledSubsystems.INTAKE_ARM_ENABLED) {
			intakeArm = new DoubleSolenoid(RobotMap.INTAKE_ARM_PCM_PORT, RobotMap.INTAKE_ARM_FORWARD,
					RobotMap.INTAKE_ARM_REVERSE);
			currentIntakeArmState = getState(intakeArm.get());
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
		intakeArm.set(DEPLOYED_VALUE);
		currentIntakeArmState = IntakeArmState.DEPLOYED;
	}

	public void retractIntakeArm() {

		Intake.getInstance().onIntakeArmRetract();

		intakeArm.set(RETRACTED_VALUE);
		currentIntakeArmState = IntakeArmState.RETRACTED;
	}

	public static IntakeArm getInstance() {
		init();
		return singleton;
	}

	@Override
	public void smartDashboardInfo() {
		if(EnabledSubsystems.INTAKEARM_SMARTDASHBOARD_BASIC_ENABLED){
			
		}
		if(EnabledSubsystems.INTAKEARM_SMARTDASHBOARD_BASIC_ENABLED){
			
		}

	}

	@Override
	public void periodic() {
	}

	@Override
	public void disable() {
		intakeArm.set(Value.kOff); //TODO: IntakeArm: Determine what state if off
	}
	
	private static IntakeArmState getState(Value val) {
		if(val == DEPLOYED_VALUE) {
			return IntakeArmState.DEPLOYED;
		} else {
			if(val == RETRACTED_VALUE) {
				return IntakeArmState.RETRACTED;
			} else {
				return IntakeArmState.RETRACTED; //TODO: IntakeArm: Determine what state if off
			}
		}
	}

	public boolean intakeArmIsDeployed() {
		return currentIntakeArmState == IntakeArmState.DEPLOYED;
	}

	public boolean intakeArmIsRetracted() {
		return currentIntakeArmState == IntakeArmState.RETRACTED;
	}

}