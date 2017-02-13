package edu.nr.robotics.subsystems.gearMover;

import edu.nr.lib.commandbased.DoNothingJoystickCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearMover extends NRSubsystem {
	public static GearMover singleton;

	public DoubleSolenoid GearMover;
	public DoubleSolenoid GearGetPosition;

	public enum GearMoverState {
		DEPLOYED, RETRACTED
	}

	public enum GearGetPositionState {
		DEPLOYED, RETRACTED
	}

	public GearMoverState currentGearMoverState = GearMoverState.RETRACTED;
	public GearGetPositionState currentGearGetPositionState = GearGetPositionState.RETRACTED;

	private GearMover() {
		if (EnabledSubsystems.GEAR_MOVER_ENABLED) {
			GearMover = new DoubleSolenoid(RobotMap.GEAR_MOVER_PNEUMATIC, RobotMap.GEAR_MOVER_FORWARD,
					RobotMap.GEAR_MOVER_REVERSE);
			GearGetPosition = new DoubleSolenoid(RobotMap.GEAR_GET_POSITION_PNEUMATIC,
					RobotMap.GEAR_GET_POSITION_FORWARD, RobotMap.GEAR_GET_POSITION_REVERSE);
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
		currentGearMoverState = GearMoverState.DEPLOYED;
	}

	public void retractGearMover() {
		GearMover.set(Value.kReverse);
		currentGearMoverState = GearMoverState.RETRACTED;
	}

	public void deployGearGetPositionOut() {
		GearGetPosition.set(Value.kForward);
		currentGearGetPositionState = GearGetPositionState.DEPLOYED;
	}

	public void retractGearGetPositionIn() {
		GearGetPosition.set(Value.kReverse);
		currentGearGetPositionState = GearGetPositionState.RETRACTED;
	}

	public static GearMover getInstance() {
		init();
		return singleton;
	}

	@Override
	public void smartDashboardInfo() {
		if(EnabledSubsystems.GEAR_MOVER_SMARTDASHBOARD_BASIC_ENABLED) {
				
		}
		if(EnabledSubsystems.GEAR_MOVER_SMARTDASHBOARD_COMPLEX_ENABLED) {
			
		}
	}

	@Override
	public void periodic() {

	}

	@Override
	public void disable() {
		GearMover.set(Value.kOff);
		GearGetPosition.set(Value.kOff);
	}

	public boolean gearMoverIsDeployed() {
		return currentGearMoverState == GearMoverState.DEPLOYED;
	}

	public boolean gearMoverIsRetracted() {
		return currentGearMoverState == GearMoverState.RETRACTED;
	}

	public boolean gearGetPositionIsDeployed() {
		return currentGearGetPositionState == GearGetPositionState.DEPLOYED;
	}

	public boolean gearGetPositionIsRetracted() {
		return currentGearGetPositionState == GearGetPositionState.RETRACTED;
	}

}