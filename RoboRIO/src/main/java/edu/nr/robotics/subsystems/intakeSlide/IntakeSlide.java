package edu.nr.robotics.subsystems.intakeSlide;

import edu.nr.lib.commandbased.DoNothingJoystickCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeSlide extends NRSubsystem {
	public static IntakeSlide singleton;

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
		if(solenoid != null) {
			return State.get(solenoid.get());
		} else {
			return State.DEPLOYED; //TODO: Should be State.RETRACTED, is deployed for testing
		}
	}


	private IntakeSlide() {
		if (EnabledSubsystems.INTAKE_SLIDE_ENABLED) {
			solenoid = new DoubleSolenoid(RobotMap.INTAKE_SLIDE_PCM_PORT, RobotMap.INTAKE_SLIDE_FORWARD,
					RobotMap.INTAKE_SLIDE_REVERSE);
		}
	}

	public static IntakeSlide getInstance() {
		if(singleton == null) {
			init();
		}
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new IntakeSlide();
			singleton.setJoystickCommand(new DoNothingJoystickCommand(singleton));
		}
	}

	void deployIntakeSlide() {
		if(solenoid != null) {
			solenoid.set(State.DEPLOYED_VALUE);
		}
	}

	void retractIntakeSlide() {
		if(solenoid != null) {
			solenoid.set(State.RETRACTED_VALUE);
		}
	}

	@Override
	public void smartDashboardInfo() {
		if(EnabledSubsystems.INTAKESLIDE_SMARTDASHBOARD_BASIC_ENABLED){
			SmartDashboard.putString("Intake Slide Position", currentState().toString());
		}
		if(EnabledSubsystems.INTAKESLIDE_SMARTDASHBOARD_BASIC_ENABLED){
			
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