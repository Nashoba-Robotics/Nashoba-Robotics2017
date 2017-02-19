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
		
		//TODO: IntakeArm: determine direction
		private static Value DEPLOYED_VALUE = Value.kForward;
		private static Value RETRACTED_VALUE = Value.kForward;
		

		
		private static State get(Value val) {
			if(val == State.DEPLOYED_VALUE) {
				return State.DEPLOYED;
			} else {
				if(val == State.RETRACTED_VALUE) {
					return State.RETRACTED;
				} else {
					return State.RETRACTED; //TODO: IntakeSlide: Determine what state if off
				}
			}
		}
	}

	public State currentState() {
		return State.get(solenoid.get());
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
		if(solenoid != null) {
			solenoid.set(Value.kOff); //TODO: IntakeSlide: Determine what state if off
		}
	}

	public boolean isDeployed() {
		return currentState() == State.DEPLOYED;
	}

}