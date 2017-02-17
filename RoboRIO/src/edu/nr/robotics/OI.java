package edu.nr.robotics;

import edu.nr.lib.commandbased.CancelAllCommand;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.gearMover.GearDeployCommand;
import edu.nr.robotics.subsystems.gearMover.GearGetPositionInCommand;
import edu.nr.robotics.subsystems.gearMover.GearGetPositionOutCommand;
import edu.nr.robotics.subsystems.gearMover.GearRetractCommand;
import edu.nr.robotics.subsystems.hood.HoodDeltaPositionCommand;
import edu.nr.robotics.subsystems.intake.IntakeJoystickCommand;
import edu.nr.robotics.subsystems.intake.IntakeSpeedCommand;
import edu.nr.robotics.subsystems.intakeArm.IntakeArmDeployCommand;
import edu.nr.robotics.subsystems.intakeArm.IntakeArmRetractCommand;
import edu.nr.robotics.subsystems.shooter.ShooterDeltaSpeedCommand;
import edu.nr.robotics.subsystems.turret.TurretPositionCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI implements SmartDashboardSource, Periodic {

	private static final double JOYSTICK_DEAD_ZONE = 0.15;

	
	//TODO: OI: Get button numbers
	/*private static final int GEAR_PEG_ALIGNMENT_BUTTON_NUMBER = -1;
	
	private static final int PUKE_BUTTON_NUMBER = -1;
	private static final int DEPLOY_INTAKE_BUTTON_NUMBER = -1;
	private static final int RETRACT_INTAKE_BUTTON_NUMBER = -1;
	private static final int INCREMENT_SHOOTER_SPEED_BUTTON_NUMBER = -1;
	private static final int DECREMENT_SHOOTER_SPEED_BUTTON_NUMBER = -1;
	private static final int INTAKE_SWITCH_BUTTON_NUMBER = -1;
	private static final int SHOOTER_SWITCH_BUTTON_NUMBER = -1;
	private static final int INCREMENT_HOOD_POSITION_BUTTON_NUMBER = -1;
	private static final int DECREMENT_HOOD_POSITION_BUTTON_NUMBER = -1;
	
	private static final int ENABLE_AUTO_TRACKING_BUTTON_NUMBER = -1;
	private static final int PRESET_TURRET_ANGLE_BLUE_BUTTON_NUMBER = -1;
	private static final int PRESET_TURRET_ANGLE_RED_BUTTON_NUMBER = -1;
	private static final int CANCEL_ALL_BUTTON_NUMBER = -1;
	private static final int SHOOT_BUTTON_NUMBER = -1;
	private static final int GEAR_DEPLOY_BUTTON_NUMBER = -1;
	private static final int GEAR_RETRACT_BUTTON_NUMBER = -1;
	private static final int GET_GEAR_IN_BUTTON_NUMBER = -1;
	private static final int GET_GEAR_OUT_BUTTON_NUMBER = -1;*/
	
	//Old driver station buttons numbers:
	private static final int GEAR_PEG_ALIGNMENT_BUTTON_NUMBER = -1;
	
	private static final int PUKE_BUTTON_NUMBER = -1;
	private static final int DEPLOY_INTAKE_BUTTON_NUMBER = -1;
	private static final int RETRACT_INTAKE_BUTTON_NUMBER = -1;
	private static final int INCREMENT_SHOOTER_SPEED_BUTTON_NUMBER = -1;
	private static final int DECREMENT_SHOOTER_SPEED_BUTTON_NUMBER = -1;
	private static final int INTAKE_SWITCH_BUTTON_NUMBER = -1;
	private static final int SHOOTER_SWITCH_BUTTON_NUMBER = -1;
	//TODO: Code dumb drive
	private static final int DUMB_DRIVE_SWITCH_BUTTON_NUMBER = -1;
	private static final int INCREMENT_HOOD_POSITION_BUTTON_NUMBER = -1;
	private static final int DECREMENT_HOOD_POSITION_BUTTON_NUMBER = -1;
	private static final int SHOOT_BUTTON_NUMBER = -1;
	
	private static final int ENABLE_AUTO_TRACKING_BUTTON_NUMBER = -1;
	private static final int PRESET_TURRET_ANGLE_BLUE_BUTTON_NUMBER = -1;
	private static final int PRESET_TURRET_ANGLE_RED_BUTTON_NUMBER = -1;
	private static final int CANCEL_ALL_BUTTON_NUMBER = -1;
	private static final int GEAR_DEPLOY_BUTTON_NUMBER = -1;
	private static final int GEAR_RETRACT_BUTTON_NUMBER = -1;
	private static final int GET_GEAR_IN_BUTTON_NUMBER = -1;
	private static final int GET_GEAR_OUT_BUTTON_NUMBER = -1;
	
	private static final int DRIVE_GEAR_TOGGLE_BUTTON_NUMBER = 1;
	
	private static final int DRIVE_REVERSE_BUTTON_NUMBER = 1;

	private double driveSpeedMultiplier = 1;

	private static OI singleton;

	private final Joystick driveLeft;
	private final Joystick driveRight;
	
	private final Joystick operatorLeft;
	private final Joystick operatorRight;
	
	private JoystickButton intakeSwitch;
	private JoystickButton shooterSwitch;

	private JoystickButton driveReverse;
	
	private OI() {		
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
		

	}

	public void initDriveRight() {
		
		driveReverse = new JoystickButton(driveRight, DRIVE_REVERSE_BUTTON_NUMBER);

		new JoystickButton(driveLeft, DRIVE_GEAR_TOGGLE_BUTTON_NUMBER).whenPressed(new NRCommand(Drive.getInstance()) {
			@Override
			public void onStart() {
				Drive.getInstance().switchGear();
			}
		});

	}
	
	public void initOperatorLeft() {
		
		new JoystickButton(operatorLeft, GEAR_PEG_ALIGNMENT_BUTTON_NUMBER).whenPressed(new GearPegAlignCommand());

		
		new JoystickButton(operatorLeft, PUKE_BUTTON_NUMBER).whenPressed(new IntakeSpeedCommand(RobotMap.INTAKE_PUKE_SPEED));
		new JoystickButton(operatorLeft, PUKE_BUTTON_NUMBER).whenReleased(new IntakeJoystickCommand());
		
		new JoystickButton(operatorLeft, DEPLOY_INTAKE_BUTTON_NUMBER).whenPressed(new IntakeArmDeployCommand());
		new JoystickButton(operatorLeft, RETRACT_INTAKE_BUTTON_NUMBER).whenPressed(new IntakeArmRetractCommand());

		new JoystickButton(operatorLeft, INCREMENT_SHOOTER_SPEED_BUTTON_NUMBER).whenPressed(new ShooterDeltaSpeedCommand(RobotMap.SHOOTER_SPEED_INCREMENT_VALUE));
		new JoystickButton(operatorLeft, DECREMENT_SHOOTER_SPEED_BUTTON_NUMBER).whenPressed(new ShooterDeltaSpeedCommand(-RobotMap.SHOOTER_SPEED_INCREMENT_VALUE));
		
		intakeSwitch = new JoystickButton(operatorLeft, INTAKE_SWITCH_BUTTON_NUMBER);
		shooterSwitch = new JoystickButton(operatorLeft, SHOOTER_SWITCH_BUTTON_NUMBER);
		
		new JoystickButton(operatorLeft, INCREMENT_HOOD_POSITION_BUTTON_NUMBER).whenPressed(new HoodDeltaPositionCommand(RobotMap.HOOD_POSITION_INCREMENT_VALUE));
		new JoystickButton(operatorLeft, DECREMENT_HOOD_POSITION_BUTTON_NUMBER).whenPressed(new HoodDeltaPositionCommand(-RobotMap.HOOD_POSITION_INCREMENT_VALUE));
		
		//TODO: Loader: See what to do with loader when shoot button is pressed
		//new JoystickButton(operatorLeft, SHOOT_BUTTON_NUMBER).();
	}

	public void initOperatorRight() {
		new JoystickButton(operatorRight, PRESET_TURRET_ANGLE_RED_BUTTON_NUMBER).whenPressed(new TurretPositionCommand(RobotMap.PRESET_TURRET_ANGLE_RED / RobotMap.DEGREES_PER_ROTATION));
		new JoystickButton(operatorRight, PRESET_TURRET_ANGLE_BLUE_BUTTON_NUMBER).whenPressed(new TurretPositionCommand(RobotMap.PRESET_TURRET_ANGLE_BLUE / RobotMap.DEGREES_PER_ROTATION));
		
		new JoystickButton(operatorRight, ENABLE_AUTO_TRACKING_BUTTON_NUMBER).whenPressed(new EnableAutoTrackingCommand());
	
		new JoystickButton(operatorRight, CANCEL_ALL_BUTTON_NUMBER).whenPressed(new CancelAllCommand());
	
		new JoystickButton(operatorRight, GEAR_DEPLOY_BUTTON_NUMBER).whenPressed(new GearDeployCommand());
		new JoystickButton(operatorRight, GEAR_RETRACT_BUTTON_NUMBER).whenPressed(new GearRetractCommand());
		
		new JoystickButton(operatorRight, GET_GEAR_IN_BUTTON_NUMBER).whenPressed(new GearGetPositionInCommand());
		new JoystickButton(operatorRight, GET_GEAR_OUT_BUTTON_NUMBER).whenPressed(new GearGetPositionOutCommand());
		
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
		return driveSpeedMultiplier * (driveReverse.get() ? -1 : 1);
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
		return snapCoffinJoysticks(operatorRight.getAxis(AxisType.kX));
	}
	
	public double getHoodValue(){
		return snapCoffinJoysticks(operatorRight.getAxis(AxisType.kY));
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
	
	public boolean isShooterOn() {
		return !shooterSwitch.get();
	}
	
	public boolean isIntakeOn() {
		return !intakeSwitch.get();
	}
}
