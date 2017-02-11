package edu.nr.robotics;

import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI implements SmartDashboardSource{

	private static final double JOYSTICK_DEAD_ZONE = 0.15;

	private double driveSpeedMultiplier = 1;

	private static OI singleton;

	private Joystick driveLeft;
	private Joystick driveRight;
	
	private Joystick operatorLeft;
	private Joystick operatorRight;

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

	}

	public void initDriveRight() {

	}
	
	public void initOperatorLeft() {

	}

	public void initOperatorRight() {

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

	public double getIntakeValue() {
		return snapCoffinJoysticks(0); //TODO: OI: Get intake joystick values
	}

	public double getLoaderValue() {
		return snapCoffinJoysticks(0); //TODO: OI: Get loader joystick values
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

	public boolean isLoaderNonZero() {
		return getLoaderValue() != 0;
	}

	public boolean isTurretNonZero() {
		return getTurretValue() != 0;
	}
	
	public boolean isHoodNonZero() {
		return getHoodValue() != 0;
	}
}
