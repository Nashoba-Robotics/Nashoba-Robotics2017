package edu.nr.robotics.subsystems.intake;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.intakeArm.IntakeArm;
import edu.nr.robotics.subsystems.intakeSlide.IntakeSlide;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends NRSubsystem {

	public static Intake singleton;

	private CANTalon lowTalon, highTalon;
	
	/**
	 * Percent
	 */
	public double lowMotorSetpoint = 0;
	
	/**
	 * Percent
	 */
	public double highMotorSetpoint = 0;

	/**
	 * The percent voltage for the intake to run at during normal usage
	 * 
	 * TODO: Intake: Get run voltage
	 */
	public static final double RUN_VOLTAGE = .75;

	/**
	 * The percent voltage for the intake to run at while attempting to "puke" all the balls.
	 * 
	 * Puking balls involves running the intake in reverse to clear any balls that are trapped in it.
	 */
	public static final double PUKE_VOLTAGE = -RUN_VOLTAGE;

	private Intake() { 
		if (EnabledSubsystems.INTAKE_ENABLED) { 
			lowTalon = new CANTalon(RobotMap.INTAKE_LOW_TALON_PORT);
			lowTalon.changeControlMode(TalonControlMode.PercentVbus);
			lowTalon.enableBrakeMode(false);
			lowTalon.setInverted(true);
			lowTalon.enable();
		
			highTalon = new CANTalon(RobotMap.INTAKE_HIGH_TALON_PORT);
			highTalon.changeControlMode(TalonControlMode.PercentVbus);
			highTalon.enableBrakeMode(false);
			highTalon.setInverted(true);
			highTalon.enable();
		}
	}

	public static Intake getInstance() {
		if(singleton == null) {
			init();
		}
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new Intake();
			getInstance().setJoystickCommand(new IntakeJoystickCommand());
		}
	}

	/**
	 * Sets the motor voltage.
	 * 
	 * If the intake arm is not deployed, the speed is set to zero instead.
	 * 
	 * @param percent
	 *            the voltage that the motor should run at, on a scale from -1 to 1
	 */
	public void setMotorVoltage(double percent) {
		lowMotorSetpoint = percent;
		highMotorSetpoint = percent;
		if (lowTalon != null && highTalon != null) {
			if(IntakeArm.getInstance().isDeployed() && IntakeSlide.getInstance().isDeployed()) {
				lowTalon.set(lowMotorSetpoint);
				highTalon.set(highMotorSetpoint);
			} else {
				lowTalon.set(0);
				highTalon.set(0);
			}
		}
	}
	
	public void onIntakeArmRetract() {
		disable();
	}
	
	/**
	 * Function that is periodically called once the subsystem class is initialized
	 */
	@Override
	public void periodic() {
		
	}

	/**
	 * Sends data to SmartDashboard
	 */
	@Override
	public void smartDashboardInfo() {
		if (lowTalon != null && highTalon != null) {
			if(EnabledSubsystems.INTAKE_SMARTDASHBOARD_BASIC_ENABLED){
				SmartDashboard.putNumber("Low Intake Current", lowTalon.getOutputCurrent());
				SmartDashboard.putNumber("High Intake Current", highTalon.getOutputCurrent());
			}
			if(EnabledSubsystems.INTAKE_SMARTDASHBOARD_COMPLEX_ENABLED){
				SmartDashboard.putNumber("Low Intake Voltage", lowTalon.getOutputVoltage());
				SmartDashboard.putNumber("High Intake Voltage", highTalon.getOutputVoltage());
			}
		}
	}

	/**
	 * What subsystem does upon robot being disabled
	 */
	@Override
	public void disable() {
		setMotorVoltage(0);
	}
	
}
