package edu.nr.robotics;

import edu.nr.lib.commandbased.CancelCommand;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.hood.HoodDeltaPositionCommand;
import edu.nr.robotics.subsystems.intake.Intake;
import edu.nr.robotics.subsystems.intake.IntakeSpeedCommand;
import edu.nr.robotics.subsystems.intakeArm.IntakeArmDeployCommand;
import edu.nr.robotics.subsystems.intakeArm.IntakeArmRetractCommand;
import edu.nr.robotics.subsystems.shooter.ShooterDeltaSpeedCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI implements SmartDashboardSource, Periodic {

	private static final double JOYSTICK_DEAD_ZONE = 0.15;

	
	//TODO: OI: Get button numbers
	private static final int PUKE_BUTTON_NUMBER = -1;
	private static final int DEPLOY_INTAKE_BUTTON_NUMBER = -1;
	private static final int RETRACT_INTAKE_BUTTON_NUMBER = -1;
	private static final int INCREMENT_SHOOTER_SPEED_BUTTON_NUMBER = -1;
	private static final int DECREMENT_SHOOTER_SPEED_BUTTON_NUMBER = -1;
	private static final int INTAKE_SWITCH_BUTTON_NUMBER = -1;
	private static final int INCREMENT_HOOD_POSITION_BUTTON_NUMBER = -1;
	private static final int DECREMENT_HOOD_POSITION_BUTTON_NUMBER = -1;
	
	private static final int ENABLE_AUTO_TRACKING_BUTTON_NUMBER = -1;
	
	private static final int GEAR_TOGGLE_BUTTON_NUMBER = -1;

	private double driveSpeedMultiplier = 1;

	private static OI singleton;

	private final Joystick driveLeft;
	private final Joystick driveRight;
	
	private final Joystick operatorLeft;
	private final Joystick operatorRight;
	
	private JoystickButton intakeSwitch;

	private OI() {
		//TODO: OI: Create buttons
		
		driveLeft = new Joystick(RobotMap.STICK_LEFT);
		driveRight = new Joystick(RobotMap.STICK_RIGHT);
		
		operatorLeft = new Joystick(RobotMap.STICK_OPERATOR_LEFT);
		operatorRight = new Joystick(RobotMap.STICK_OPERATOR_RIGHT);

		initDriveLeft();
		initDriveRight();
		
		initOperatorLeft();
		initOperatorRight();
	}

	public void initDriveLeft() {
		
		new JoystickButton(driveLeft, GEAR_TOGGLE_BUTTON_NUMBER).toggleWhenPressed(new NRCommand(Drive.getInstance()) {
			@Override
			public void onStart() {
				Drive.getInstance().switchGear();
			}
		});

	}

	public void initDriveRight() {

	}
	
	public void initOperatorLeft() {
		
		new JoystickButton(operatorLeft, PUKE_BUTTON_NUMBER).whenPressed(new IntakeSpeedCommand(RobotMap.INTAKE_PUKE_SPEED));
		new JoystickButton(operatorLeft, PUKE_BUTTON_NUMBER).whenReleased(new CancelCommand(Intake.getInstance()));
		
		new JoystickButton(operatorLeft, DEPLOY_INTAKE_BUTTON_NUMBER).whenPressed(new IntakeArmDeployCommand());
		new JoystickButton(operatorLeft, RETRACT_INTAKE_BUTTON_NUMBER).whenPressed(new IntakeArmRetractCommand());

		new JoystickButton(operatorLeft, INCREMENT_SHOOTER_SPEED_BUTTON_NUMBER).whenPressed(new ShooterDeltaSpeedCommand(RobotMap.SHOOTER_SPEED_INCREMENT_VALUE));
		new JoystickButton(operatorLeft, DECREMENT_SHOOTER_SPEED_BUTTON_NUMBER).whenPressed(new ShooterDeltaSpeedCommand(-RobotMap.SHOOTER_SPEED_INCREMENT_VALUE));
		
		intakeSwitch = new JoystickButton(operatorLeft, INTAKE_SWITCH_BUTTON_NUMBER);
		
		new JoystickButton(operatorLeft, INCREMENT_HOOD_POSITION_BUTTON_NUMBER).whenPressed(new HoodDeltaPositionCommand(RobotMap.HOOD_POSITION_INCREMENT_VALUE));
		new JoystickButton(operatorLeft, DECREMENT_HOOD_POSITION_BUTTON_NUMBER).whenPressed(new HoodDeltaPositionCommand(-RobotMap.HOOD_POSITION_INCREMENT_VALUE));
		
	}

	public void initOperatorRight() {
		new JoystickButton(operatorRight, ENABLE_AUTO_TRACKING_BUTTON_NUMBER).whenPressed(new EnableAutoTrackingCommand());
	}

	public static OI getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new OI();
		}
	}
	
	public double getArcadeMoveValue() {
		return snapDriveJoysticks(driveLeft.getY()) * (driveLeft.getRawButton(2) ? 1 : -1);
	}

	public double getArcadeTurnValue() {
		return snapDriveJoysticks(driveRight.getX()) * getTurnAdjust();
	}

	public double getTankLeftValue() {
		return snapDriveJoysticks(driveLeft.getY());
	}

	public double getTankRightValue() {
		return snapDriveJoysticks(driveRight.getY());
	}

	public double getDriveLeftXValue() {
		return snapDriveJoysticks(driveLeft.getX());
	}

	public double getDriveLeftYValue() {
		return snapDriveJoysticks(driveLeft.getY());
	}
	
	public double getDriveRightXValue() {
		return snapDriveJoysticks(driveRight.getX());
	}

	public double getDriveRightYValue() {
		return snapDriveJoysticks(driveRight.getY());
	}
	
	public double getDriveSpeedMultiplier() {
		return driveSpeedMultiplier * (driveLeft.getButton(Joystick.ButtonType.kTrigger) ? -1 : 1);
	}

	/**
	 * Get the current speed the intake should be moving at. 
	 * 
	 * If the switch is pressed, we should be running, otherwise we should be stopped.
	 * 
	 * @return The speed, in rotations per minute
	 */
	public double getIntakeValue() {
		return intakeSwitch.get() ? RobotMap.INTAKE_RUN_SPEED : 0;
	}
	
	public double getTurretValue() {
		return snapCoffinJoysticks(0); //TODO: OI: Get turret joystick values
	}
	
	public double getHoodValue(){
		return snapCoffinJoysticks(0); //TODO: OI: Get hood joystick values
	}

	private static double snapDriveJoysticks(double value) {
		if (Math.abs(value) < JOYSTICK_DEAD_ZONE) {
			value = 0;
		} else if (value > 0) {
			value -= JOYSTICK_DEAD_ZONE;
		} else {
			value += JOYSTICK_DEAD_ZONE;
		}
		value /= 1 - JOYSTICK_DEAD_ZONE;
		return value;
	}

	private static double snapCoffinJoysticks(double value) {
		if(value > -0.5 && value < 0.5)
			return 0;
		return ((Math.abs(value)-0.5) / 0.5) * Math.signum(value);
	}

	public double getRawMove() {
		return driveLeft.getY();
	}

	public double getRawTurn() {
		return driveRight.getX();
	}

	private double getTurnAdjust() {
		return driveRight.getRawButton(1) ? 0.5 : 1;
	}

	@Override
	public void smartDashboardInfo() {
		driveSpeedMultiplier = SmartDashboard.getNumber("Speed Multiplier", 0);
	}

	public boolean isTankNonZero() {
		return getTankLeftValue() != 0 || getTankRightValue() != 0;
	}

	public boolean isArcadeNonZero() {
		return getArcadeMoveValue() != 0 || getArcadeTurnValue() != 0;
	}

	public boolean isDriveNonZero() {
		return getDriveLeftXValue() != 0 || getDriveRightXValue() != 0 || getDriveLeftYValue() != 0 || getDriveRightYValue() != 0;
	}

	public boolean isIntakeNonZero() {
		return getIntakeValue() != 0;
	}

	public boolean isTurretNonZero() {
		return getTurretValue() != 0;
	}
	
	public boolean isHoodNonZero() {
		return getHoodValue() != 0;
	}

	@Override
	public void periodic() {
		
		
		
	}
}
