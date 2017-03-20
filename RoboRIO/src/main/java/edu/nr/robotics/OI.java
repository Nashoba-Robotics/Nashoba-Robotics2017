package edu.nr.robotics;

import edu.nr.lib.HeldJoystickButton;
import edu.nr.lib.commandbased.AnonymousCommandGroup;
import edu.nr.lib.commandbased.CancelAllCommand;
import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.multicommands.ClimbCommand;
import edu.nr.robotics.multicommands.EnableAutoTrackingCommand;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.multicommands.WallShotAlignCommand;
import edu.nr.robotics.subsystems.compressor.CompressorToggleCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveForwardForeverBasicCommand;
import edu.nr.robotics.subsystems.drive.DriveHighGearCommand;
import edu.nr.robotics.subsystems.drive.DriveJoystickCommand;
import edu.nr.robotics.subsystems.drive.DriveLowGearCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.nr.robotics.subsystems.drive.DriveTurnConstantSpeedCommand;
import edu.nr.robotics.subsystems.gearMover.GearDeployCommand;
import edu.nr.robotics.subsystems.gearMover.GearFlapInCommand;
import edu.nr.robotics.subsystems.gearMover.GearFlapOutCommand;
import edu.nr.robotics.subsystems.gearMover.GearRetractCommand;
import edu.nr.robotics.subsystems.hood.HoodDeltaPositionCommand;
import edu.nr.robotics.subsystems.hood.HoodPositionCommand;
import edu.nr.robotics.subsystems.hood.HoodSpeedCommand;
import edu.nr.robotics.subsystems.intake.Intake;
import edu.nr.robotics.subsystems.intake.IntakeSpeedCommand;
import edu.nr.robotics.subsystems.intakeArm.IntakeArmDeployCommand;
import edu.nr.robotics.subsystems.intakeArm.IntakeArmRetractCommand;
import edu.nr.robotics.subsystems.intakeSlide.IntakeSlideDeployCommand;
import edu.nr.robotics.subsystems.intakeSlide.IntakeSlideRetractCommand;
import edu.nr.robotics.subsystems.loader.LoaderRunCommand;
import edu.nr.robotics.subsystems.loader.LoaderStopCommand;
import edu.nr.robotics.subsystems.shooter.ShooterDeltaSpeedCommand;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.nr.robotics.subsystems.turret.TurretPositionCommand;
import edu.nr.robotics.subsystems.turret.TurretSpeedCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI implements SmartDashboardSource {

	private static final double JOYSTICK_DEAD_ZONE = 0.1;

	
	private static final int GEAR_PEG_ALIGNMENT_BUTTON_NUMBER = 12;
	
	private static final int PUKE_BUTTON_NUMBER = 5;
	private static final int DEPLOY_INTAKE_BUTTON_NUMBER = 4;
	private static final int RETRACT_INTAKE_BUTTON_NUMBER = 6;
	private static final int INCREMENT_SHOOTER_SPEED_BUTTON_NUMBER = 11;
	private static final int DECREMENT_SHOOTER_SPEED_BUTTON_NUMBER = 10;
	private static final int INCREMENT_HOOD_POSITION_BUTTON_NUMBER = 6;
	private static final int DECREMENT_HOOD_POSITION_BUTTON_NUMBER = 4;
	private static final int SHOOT_BUTTON_NUMBER = 10;
	private static final int THREE_SIXTY_NO_SCOPE_BUTTON_NUMBER = 7;
	private static final int TURN_OFF_COMPRESSOR_BUTTON_NUMBER = 7;
	
	private static final int ENABLE_AUTO_TRACKING_BUTTON_NUMBER = 2;
	private static final int PRESET_TURRET_ANGLE_BLUE_BUTTON_NUMBER = 3;
	private static final int PRESET_TURRET_ANGLE_RED_BUTTON_NUMBER = 5;
	private static final int CANCEL_ALL_BUTTON_NUMBER = 1;
	
	private static final int AGITATOR_SWITCH_BUTTON_NUMBER = 1;
	private static final int SHOOTER_SWITCH_BUTTON_NUMBER = 2;
	private static final int INTAKE_SWITCH_BUTTON_NUMBER = 3;
	
	private static final int GEAR_DEPLOY_BUTTON_NUMBER = 9;
	private static final int GEAR_RETRACT_BUTTON_NUMBER = 12;
	private static final int FLAP_IN_BUTTON_NUMBER = 11;
	private static final int FLAP_OUT_BUTTON_NUMBER = 8;
	
	private static final int WALL_SHOT_BUTTON_NUMBER = 9;

	
	private static final int DRIVE_LOW_GEAR_BUTTON = 3;
	private static final int DRIVE_HIGH_GEAR_BUTTON = 4;
	
	private static final int DRIVE_REVERSE_BUTTON_NUMBER = 1;

	private double driveSpeedMultiplier = 1;

	private static OI singleton;

	private final Joystick driveLeft;
	private final Joystick driveRight;
	
	private final Joystick operatorLeft;
	private final Joystick operatorRight;
	
	private JoystickButton intakeSwitch;
	private JoystickButton shooterSwitch;
	private JoystickButton agitatorSwitch;
	private JoystickButton dumbDriveSwitch;
		
	private static final int STICK_LEFT = 0;
	private static final int STICK_RIGHT = 1;
	private static final int STICK_OPERATOR_LEFT = 2;
	private static final int STICK_OPERATOR_RIGHT = 3;
	
	private Time changeShooterWaitTime = new Time(400, Time.Unit.MILLISECOND);
	private Time changeHoodWaitTime = new Time(400, Time.Unit.MILLISECOND);

	/**
	 * The change in position that will occur whenever the hood position increment or decrement button is pressed.
	 */
	public static final Angle HOOD_POSITION_INCREMENT_VALUE = new Angle(2, Angle.Unit.DEGREE);


	/**
	 * The change in speed that will occur whenever the shooter speed increment or decrement button is pressed.
	 */
	public static final AngularSpeed SHOOTER_SPEED_INCREMENT_VALUE = new AngularSpeed(10, Angle.Unit.ROTATION, Time.Unit.MINUTE);


	/**
	 * What {@link Drive#DriveMode} for the {@link DriveJoystickCommand} to use.
	 */
	public static final Drive.DriveMode driveMode = Drive.DriveMode.arcadeDrive;


	private static final int DRIVE_TURN_SLOW_BUTTON_NUMBER = 3;
	
	private OI() {		
		driveLeft = new Joystick(STICK_LEFT);
		driveRight = new Joystick(STICK_RIGHT);
		
		operatorLeft = new Joystick(STICK_OPERATOR_LEFT);
		operatorRight = new Joystick(STICK_OPERATOR_RIGHT);

		initDriveLeft();
		initDriveRight();
		
		initOperatorLeft();
		initOperatorRight();
		
		SmartDashboardSource.sources.add(this);
	}

	public void initDriveLeft() {
		new JoystickButton(driveLeft, 2).whenPressed(new DoNothingCommand(Drive.getInstance()));
		new JoystickButton(driveLeft, 11).whenPressed(new TurretSpeedCommand(new AngularSpeed(3, Angle.Unit.DEGREE, Time.Unit.SECOND)));
		new JoystickButton(driveLeft, 16).whenPressed(new TurretSpeedCommand(new AngularSpeed(-3, Angle.Unit.DEGREE, Time.Unit.SECOND)));
		new JoystickButton(driveLeft, 12).whenPressed(new HoodSpeedCommand(new AngularSpeed(6, Angle.Unit.DEGREE, Time.Unit.SECOND)));
		new JoystickButton(driveLeft, 15).whenPressed(new HoodSpeedCommand(new AngularSpeed(-6, Angle.Unit.DEGREE, Time.Unit.SECOND)));
		new JoystickButton(driveLeft, 9).whenPressed(new HoodPositionCommand(new Angle(10, Angle.Unit.DEGREE)));

	}

	public void initDriveRight() {
		
		new JoystickButton(driveRight, DRIVE_LOW_GEAR_BUTTON).whenPressed(new DriveLowGearCommand());

		new JoystickButton(driveRight, DRIVE_HIGH_GEAR_BUTTON).whenPressed(new DriveHighGearCommand());

		new JoystickButton(driveRight, 2).whenPressed(new ClimbCommand());
	}
	
	public void initOperatorLeft() {

				
		new JoystickButton(operatorLeft, PUKE_BUTTON_NUMBER).whenPressed(new IntakeSpeedCommand(Intake.PUKE_VOLTAGE));
		new JoystickButton(operatorLeft, PUKE_BUTTON_NUMBER).whenReleased(new DoNothingCommand(Intake.getInstance()));
		
		new JoystickButton(operatorLeft, DEPLOY_INTAKE_BUTTON_NUMBER).whenPressed(new IntakeArmDeployCommand());
		new JoystickButton(operatorLeft, DEPLOY_INTAKE_BUTTON_NUMBER).whenPressed(new GearFlapInCommand());
		new JoystickButton(operatorLeft, DEPLOY_INTAKE_BUTTON_NUMBER).whenPressed(new AnonymousCommandGroup() {
			public void commands() {
				addSequential(new WaitCommand(0.2));
				addSequential(new DoNothingCommand(Intake.getInstance()));
			}
		});
		new JoystickButton(operatorLeft, DEPLOY_INTAKE_BUTTON_NUMBER).whenPressed(new AnonymousCommandGroup() {
			public void commands() {
				addSequential(new WaitCommand(0.2));
				addSequential(new IntakeSlideDeployCommand());
			}
		});
		
		new JoystickButton(operatorLeft, RETRACT_INTAKE_BUTTON_NUMBER).whenPressed(new IntakeSlideRetractCommand());
		new JoystickButton(operatorLeft, RETRACT_INTAKE_BUTTON_NUMBER).whenPressed(new AnonymousCommandGroup() {
			public void commands() {
				addSequential(new WaitCommand(0.3));
				addSequential(new IntakeSpeedCommand(0));
			}
		});
		new JoystickButton(operatorLeft, RETRACT_INTAKE_BUTTON_NUMBER).whenPressed(new AnonymousCommandGroup() {
			public void commands() {
				addSequential(new WaitCommand(0.4));
				addSequential(new IntakeArmRetractCommand());
			}
		});
		
		new JoystickButton(operatorLeft, SHOOT_BUTTON_NUMBER).whenPressed(new LoaderRunCommand());
		new JoystickButton(operatorLeft, SHOOT_BUTTON_NUMBER).whenReleased(new LoaderStopCommand());
		
		new JoystickButton(operatorLeft, GEAR_DEPLOY_BUTTON_NUMBER).whenPressed(new GearDeployCommand());
		new JoystickButton(operatorLeft, GEAR_RETRACT_BUTTON_NUMBER).whenPressed(new GearRetractCommand());
		
		new JoystickButton(operatorLeft, FLAP_IN_BUTTON_NUMBER).whenPressed(new GearFlapInCommand());
		
		new JoystickButton(operatorLeft, FLAP_OUT_BUTTON_NUMBER).whenPressed(new IntakeSlideRetractCommand());
		new JoystickButton(operatorLeft, FLAP_OUT_BUTTON_NUMBER).whenPressed(new AnonymousCommandGroup() {
			public void commands() {
				addSequential(new WaitCommand(0.3));
				addSequential(new IntakeSpeedCommand(0));
			}
		});
		new JoystickButton(operatorLeft, FLAP_OUT_BUTTON_NUMBER).whenPressed(new AnonymousCommandGroup() {
			public void commands() {
				addSequential(new WaitCommand(0.5));
				addSequential(new IntakeArmRetractCommand());
			}
		});
		new JoystickButton(operatorLeft, FLAP_OUT_BUTTON_NUMBER).whenPressed(new AnonymousCommandGroup() {
			public void commands() {
				addSequential(new WaitCommand(0.0)); //TODO: VERY IMPORTANT: ONCE WE HAVE INTAKE, THIS NEEDS TO BE ONE SECOND
				addSequential(new GearFlapOutCommand());
			}
		});

		new JoystickButton(operatorLeft, THREE_SIXTY_NO_SCOPE_BUTTON_NUMBER).whenPressed(new DrivePIDTurnAngleCommand());
		//new JoystickButton(operatorLeft, THREE_SIXTY_NO_SCOPE_BUTTON_NUMBER).whenPressed(new DriveTurnConstantSpeedCommand(0.3));
		//new JoystickButton(operatorLeft, THREE_SIXTY_NO_SCOPE_BUTTON_NUMBER).whenReleased(new DoNothingCommand(Drive.getInstance()));
		
		agitatorSwitch = new JoystickButton(operatorLeft, AGITATOR_SWITCH_BUTTON_NUMBER);
		intakeSwitch = new JoystickButton(operatorLeft, INTAKE_SWITCH_BUTTON_NUMBER);
		shooterSwitch = new JoystickButton(operatorLeft, SHOOTER_SWITCH_BUTTON_NUMBER);


	}

	public void initOperatorRight() {
		
		new JoystickButton(operatorRight, GEAR_PEG_ALIGNMENT_BUTTON_NUMBER).whenPressed(new GearPegAlignCommand());
		new JoystickButton(operatorRight, GEAR_PEG_ALIGNMENT_BUTTON_NUMBER).whenReleased(new DoNothingCommand(Drive.getInstance()));


		new JoystickButton(operatorRight, PRESET_TURRET_ANGLE_RED_BUTTON_NUMBER).whenPressed(new TurretPositionCommand(Turret.PRESET_ANGLE_RED));
		new JoystickButton(operatorRight, PRESET_TURRET_ANGLE_BLUE_BUTTON_NUMBER).whenPressed(new TurretPositionCommand(Turret.PRESET_ANGLE_BLUE));
		
		new JoystickButton(operatorRight, ENABLE_AUTO_TRACKING_BUTTON_NUMBER).whenPressed(new EnableAutoTrackingCommand());
	
		new JoystickButton(operatorRight, CANCEL_ALL_BUTTON_NUMBER).whenPressed(new CancelAllCommand());
	
		
		new JoystickButton(operatorRight, WALL_SHOT_BUTTON_NUMBER).whenPressed(new WallShotAlignCommand());
		
		new HeldJoystickButton(operatorRight, INCREMENT_SHOOTER_SPEED_BUTTON_NUMBER, changeShooterWaitTime).initialThenRepeat(new ShooterDeltaSpeedCommand(OI.SHOOTER_SPEED_INCREMENT_VALUE));
		new HeldJoystickButton(operatorRight, DECREMENT_SHOOTER_SPEED_BUTTON_NUMBER, changeShooterWaitTime).initialThenRepeat(new ShooterDeltaSpeedCommand(OI.SHOOTER_SPEED_INCREMENT_VALUE.negate()));

		new HeldJoystickButton(operatorRight, INCREMENT_HOOD_POSITION_BUTTON_NUMBER, changeHoodWaitTime).initialThenRepeat(new HoodDeltaPositionCommand(OI.HOOD_POSITION_INCREMENT_VALUE));
		new HeldJoystickButton(operatorRight, DECREMENT_HOOD_POSITION_BUTTON_NUMBER, changeHoodWaitTime).initialThenRepeat(new HoodDeltaPositionCommand(OI.HOOD_POSITION_INCREMENT_VALUE.negate()));

		new JoystickButton(operatorRight, TURN_OFF_COMPRESSOR_BUTTON_NUMBER).whenPressed(new CompressorToggleCommand());
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
		return snapDriveJoysticks(driveRight.getX()) * getTurnAdjust() * (driveLeft.getRawButton(DRIVE_REVERSE_BUTTON_NUMBER) ? 1 : -1);
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
	
	// -> Joy2: Loader Roller Joystick
	// Overrides loader motor power
	public double getTurretValue() {
		return snapCoffinJoysticks(operatorRight.getAxis(AxisType.kX));
	}

	// -> Joy3: Hood Joystick
	// Overrides hood angle (undone if another auto hood angle command is
	// sent)
	public double getHoodValue() {
		return -snapCoffinJoysticks(operatorLeft.getAxis(AxisType.kX));
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
		double snapValue = 0.2;
		
		if(value > -snapValue && value < snapValue)
			return 0;
		return ((Math.abs(value)-snapValue) / (1-snapValue)) * Math.signum(value);
	}

	public double getRawMove() {
		return driveLeft.getY();
	}

	public double getRawTurn() {
		return driveRight.getX();
	}

	private double getTurnAdjust() {
		return driveRight.getRawButton(DRIVE_TURN_SLOW_BUTTON_NUMBER) ? 0.5 : 1;
	}

	@Override
	public void smartDashboardInfo() {
		//driveSpeedMultiplier = SmartDashboard.getNumber("Speed Multiplier", 1);
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
	
	public boolean isShooterOn() {
		//System.out.println("Shooter on: " + !shooterSwitch.get());
		return !shooterSwitch.get();
	}
	
	public boolean isAgitatorOn() {
		//System.out.println("Agitator on: " + !agitatorSwitch.get());
		return !agitatorSwitch.get();
	}
	
	public boolean isIntakeOn() {
		return intakeSwitch.get();
	}

	public boolean shouldDumbDrive() {
		return dumbDriveSwitch.get();
	}
}
