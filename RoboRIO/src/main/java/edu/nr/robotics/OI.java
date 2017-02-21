package edu.nr.robotics;

import edu.nr.lib.commandbased.CancelAllCommand;
import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveJoystickCommand;
import edu.nr.robotics.subsystems.gearMover.GearDeployCommand;
import edu.nr.robotics.subsystems.gearMover.GearGetPositionInCommand;
import edu.nr.robotics.subsystems.gearMover.GearGetPositionOutCommand;
import edu.nr.robotics.subsystems.gearMover.GearRetractCommand;
import edu.nr.robotics.subsystems.hood.HoodDeltaPositionCommand;
import edu.nr.robotics.subsystems.intake.Intake;
import edu.nr.robotics.subsystems.intake.IntakeSpeedCommand;
import edu.nr.robotics.subsystems.intakeArm.IntakeArmDeployCommand;
import edu.nr.robotics.subsystems.intakeArm.IntakeArmRetractCommand;
import edu.nr.robotics.subsystems.loader.LoaderRunCommand;
import edu.nr.robotics.subsystems.loader.LoaderStopCommand;
import edu.nr.robotics.subsystems.shooter.ShooterDeltaSpeedCommand;
import edu.nr.robotics.subsystems.turret.Turret;
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
	private static final int GEAR_PEG_ALIGNMENT_BUTTON_NUMBER = 2;
	
	private static final int PUKE_BUTTON_NUMBER = 12;
	private static final int DEPLOY_INTAKE_BUTTON_NUMBER = 11;
	private static final int RETRACT_INTAKE_BUTTON_NUMBER = 10;
	private static final int INCREMENT_SHOOTER_SPEED_BUTTON_NUMBER = 4;
	private static final int DECREMENT_SHOOTER_SPEED_BUTTON_NUMBER = 6;
	private static final int INTAKE_SWITCH_BUTTON_NUMBER = 11;
	private static final int SHOOTER_SWITCH_BUTTON_NUMBER = 12;
	private static final int DUMB_DRIVE_SWITCH_BUTTON_NUMBER = 3;
	private static final int INCREMENT_HOOD_POSITION_BUTTON_NUMBER = 5;
	private static final int DECREMENT_HOOD_POSITION_BUTTON_NUMBER = 9;
	private static final int SHOOT_BUTTON_NUMBER = 1;
	
	private static final int ENABLE_AUTO_TRACKING_BUTTON_NUMBER = 5;
	private static final int PRESET_TURRET_ANGLE_BLUE_BUTTON_NUMBER = 9;
	private static final int PRESET_TURRET_ANGLE_RED_BUTTON_NUMBER = 8;
	private static final int CANCEL_ALL_BUTTON_NUMBER = 7;
	private static final int GEAR_DEPLOY_BUTTON_NUMBER = 4;
	private static final int GEAR_RETRACT_BUTTON_NUMBER = 3;
	private static final int GET_GEAR_IN_BUTTON_NUMBER = 2;
	private static final int GET_GEAR_OUT_BUTTON_NUMBER = 1;
	
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
	private JoystickButton dumbDriveSwitch;
		
	// TODO: OI: Get actual Joystick ports
	private static final int STICK_LEFT = -1;
	private static final int STICK_RIGHT = -1;
	private static final int STICK_OPERATOR_LEFT = 0;
	private static final int STICK_OPERATOR_RIGHT = 0;


	/**
	 * The change in position that will occur whenever the hood position increment or decrement button is pressed.
	 */
	public static final Angle HOOD_POSITION_INCREMENT_VALUE = new Angle(0.5, Angle.Unit.DEGREE);


	/**
	 * The change in speed that will occur whenever the shooter speed increment or decrement button is pressed.
	 */
	public static final AngularSpeed SHOOTER_SPEED_INCREMENT_VALUE = new AngularSpeed(100, AngularSpeed.Unit.RPM);


	/**
	 * What {@link Drive#DriveMode} for the {@link DriveJoystickCommand} to use.
	 */
	public static final Drive.DriveMode driveMode = Drive.DriveMode.arcadeDrive;
	
	private OI() {		
		driveLeft = new Joystick(STICK_LEFT);
		driveRight = new Joystick(STICK_RIGHT);
		
		operatorLeft = new Joystick(STICK_OPERATOR_LEFT);
		operatorRight = new Joystick(STICK_OPERATOR_RIGHT);

		initDriveLeft();
		initDriveRight();
		
		initOperatorLeft();
		initOperatorRight();
	}

	public void initDriveLeft() {
		

	}

	public void initDriveRight() {
		
		new JoystickButton(driveLeft, DRIVE_GEAR_TOGGLE_BUTTON_NUMBER).whenPressed(new NRCommand(Drive.getInstance()) {
			@Override
			public void onStart() {
				Drive.getInstance().switchGear();
			}
		});

	}
	
	public void initOperatorLeft() {
		
		dumbDriveSwitch = new JoystickButton(operatorLeft, DUMB_DRIVE_SWITCH_BUTTON_NUMBER);
		
		dumbDriveSwitch.whenPressed(new NRCommand(Drive.getInstance()) {
			@Override
			public void onStart() {
				Drive.getInstance().startDumbDrive();
			}
		});

		dumbDriveSwitch.whenReleased(new NRCommand(Drive.getInstance()) {
			@Override
			public void onStart() {
				Drive.getInstance().endDumbDrive();
			}
		});

		
		new JoystickButton(operatorLeft, GEAR_PEG_ALIGNMENT_BUTTON_NUMBER).whenPressed(new GearPegAlignCommand(false));

		
		new JoystickButton(operatorLeft, PUKE_BUTTON_NUMBER).whenPressed(new IntakeSpeedCommand(Intake.PUKE_VOLTAGE));
		new JoystickButton(operatorLeft, PUKE_BUTTON_NUMBER).whenReleased(new DoNothingCommand(Intake.getInstance()));
		
		new JoystickButton(operatorLeft, DEPLOY_INTAKE_BUTTON_NUMBER).whenPressed(new IntakeArmDeployCommand());
		new JoystickButton(operatorLeft, RETRACT_INTAKE_BUTTON_NUMBER).whenPressed(new IntakeArmRetractCommand());

		new JoystickButton(operatorLeft, INCREMENT_SHOOTER_SPEED_BUTTON_NUMBER).whileHeld(new ShooterDeltaSpeedCommand(OI.SHOOTER_SPEED_INCREMENT_VALUE));
		new JoystickButton(operatorLeft, DECREMENT_SHOOTER_SPEED_BUTTON_NUMBER).whileHeld(new ShooterDeltaSpeedCommand(OI.SHOOTER_SPEED_INCREMENT_VALUE.negate()));
		
		
		new JoystickButton(operatorLeft, INCREMENT_HOOD_POSITION_BUTTON_NUMBER).whileHeld(new HoodDeltaPositionCommand(OI.HOOD_POSITION_INCREMENT_VALUE));
		new JoystickButton(operatorLeft, DECREMENT_HOOD_POSITION_BUTTON_NUMBER).whileHeld(new HoodDeltaPositionCommand(OI.HOOD_POSITION_INCREMENT_VALUE.negate()));
		
		new JoystickButton(operatorLeft, SHOOT_BUTTON_NUMBER).whenPressed(new LoaderRunCommand());
		new JoystickButton(operatorLeft, SHOOT_BUTTON_NUMBER).whenReleased(new LoaderStopCommand());
	}

	public void initOperatorRight() {
		new JoystickButton(operatorRight, PRESET_TURRET_ANGLE_RED_BUTTON_NUMBER).whenPressed(new TurretPositionCommand(Turret.PRESET_ANGLE_RED));
		new JoystickButton(operatorRight, PRESET_TURRET_ANGLE_BLUE_BUTTON_NUMBER).whenPressed(new TurretPositionCommand(Turret.PRESET_ANGLE_BLUE));
		
		new JoystickButton(operatorRight, ENABLE_AUTO_TRACKING_BUTTON_NUMBER).whenPressed(new EnableAutoTrackingCommand());
	
		new JoystickButton(operatorRight, CANCEL_ALL_BUTTON_NUMBER).whenPressed(new CancelAllCommand());
	
		new JoystickButton(operatorRight, GEAR_DEPLOY_BUTTON_NUMBER).whenPressed(new GearDeployCommand());
		new JoystickButton(operatorRight, GEAR_RETRACT_BUTTON_NUMBER).whenPressed(new GearRetractCommand());
		
		new JoystickButton(operatorRight, GET_GEAR_IN_BUTTON_NUMBER).whenPressed(new GearGetPositionInCommand());
		new JoystickButton(operatorRight, GET_GEAR_OUT_BUTTON_NUMBER).whenPressed(new GearGetPositionOutCommand());
		
		intakeSwitch = new JoystickButton(operatorRight, INTAKE_SWITCH_BUTTON_NUMBER);
		shooterSwitch = new JoystickButton(operatorRight, SHOOTER_SWITCH_BUTTON_NUMBER);

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
		return snapDriveJoysticks(driveLeft.getY()) * (driveLeft.getRawButton(DRIVE_REVERSE_BUTTON_NUMBER) ? 1 : -1);
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
		return driveSpeedMultiplier;
	}

	/**
	 * Get the current speed the intake should be moving at. 
	 * 
	 * If the switch is pressed, we should be running, otherwise we should be stopped.
	 * 
	 * @return The speed, in rotations per minute
	 */
	public double getIntakeValue() {
		return intakeSwitch.get() ? Intake.RUN_VOLTAGE : 0;
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

	public boolean shouldDumbDrive() {
		return dumbDriveSwitch.get();
	}
}
