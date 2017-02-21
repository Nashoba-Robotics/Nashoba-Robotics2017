package edu.nr.robotics.subsystems.intakeArm;

import edu.nr.lib.commandbased.DoNothingJoystickCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeArm extends NRSubsystem {
	public static IntakeArm singleton;

	private DoubleSolenoid solenoid;

	public enum State {
		DEPLOYED, RETRACTED;
		
		private static Value DEPLOYED_VALUE = Value.kForward;
		private static Value RETRACTED_VALUE = Value.kReverse;
		

		
		private static State get(Value val) {
			if(val == State.DEPLOYED_VALUE) {
				return State.DEPLOYED;
			} else {
				return State.RETRACTED;
			}
		}
	}

	public State currentState() {
		return State.get(solenoid.get());
	}


	private IntakeArm() {
		if (EnabledSubsystems.INTAKE_ARM_ENABLED) {
			solenoid = new DoubleSolenoid(RobotMap.INTAKE_ARM_PCM_PORT, RobotMap.INTAKE_ARM_FORWARD,
					RobotMap.INTAKE_ARM_REVERSE);
		}
	}

	public static IntakeArm getInstance() {
		if(singleton == null) {
			init();
		}
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new IntakeArm();
			singleton.setJoystickCommand(new DoNothingJoystickCommand(singleton));
		}
	}

	void deployIntakeArm() {
		if(solenoid != null) {
			solenoid.set(State.DEPLOYED_VALUE);
		}
	}

	void retractIntakeArm() {
		if(solenoid != null) {
			solenoid.set(State.RETRACTED_VALUE);
		}
	}

	@Override
	public void smartDashboardInfo() {
		if(EnabledSubsystems.INTAKEARM_SMARTDASHBOARD_BASIC_ENABLED){
			SmartDashboard.putString("Intake Arm Position", currentState().toString());
		}
		if(EnabledSubsystems.INTAKEARM_SMARTDASHBOARD_BASIC_ENABLED){
			
		}

	}

	@Override
	public void periodic() {
	}

	@Override
	public void disable() {
	}

	public boolean isDeployed() {
		return currentState() == State.DEPLOYED;
	}

}