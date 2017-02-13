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
	
	public enum State {
		DEPLOYED, RETRACTED
	}
	
	/**
	 * The current state of the intake arm, either retracted or deployed.
	 * 
	 * The default is retracted, since that is how the robot will start.
	 */
	public State currentState = State.RETRACTED;

	private IntakeArm() {
		if (EnabledSubsystems.INTAKE_ARM_ENABLED) {
			intakeArm = new DoubleSolenoid(RobotMap.INTAKE_ARM_PNEUMATIC, RobotMap.INTAKE_ARM_FORWARD,
					RobotMap.INTAKE_ARM_REVERSE);
			//TODO: IntakeArm: Check solenoid for current state

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
		currentState = State.DEPLOYED;
	}

	public void retractIntakeArm() {
		
		Intake.getInstance().onIntakeArmRetract();
		
		intakeArm.set(Value.kReverse);
		currentState = State.RETRACTED;
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
		intakeArm.set(Value.kOff);
	}

	public boolean isDeployed() {
		return currentState == State.DEPLOYED;
	}

	public boolean isRetracted() {
		return currentState == State.RETRACTED;
	}

}