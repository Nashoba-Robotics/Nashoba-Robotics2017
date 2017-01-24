package edu.nr.robotics;

import edu.wpi.first.wpilibj.SampleRobot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	public CANTalon shooterTalon;

	private double shooterMotorSetPoint = 0;

	public double turn_F_SHOOTER = 0.17; //2.3 for Reuben
	public double turn_P_SHOOTER = 1.5;
	public double turn_I_SHOOTER = 0;
	public double turn_D_SHOOTER = 0;

	private static final int ticksPerRev = 256;


	@Override
	public void robotInit() {
		shooterTalon = new CANTalon(RobotMap.shooterTalon);
		shooterTalon.enableBrakeMode(false);
		shooterTalon.changeControlMode(TalonControlMode.Speed);
		shooterTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		shooterTalon.setF(turn_F_SHOOTER);
		shooterTalon.setP(turn_P_SHOOTER);
		shooterTalon.setI(turn_I_SHOOTER);
		shooterTalon.setD(turn_D_SHOOTER);
		shooterTalon.configEncoderCodesPerRev(ticksPerRev);
		
		shooterTalon.reverseSensor(true);
		
		SmartDashboard.putNumber("Goal Shooter Speed", 0);	
	}

	public void setMotorSpeed(double speed) {
		shooterMotorSetPoint = speed;
		SmartDashboard.putString("Shooter Speed String",
				Math.abs(shooterTalon.getSpeed()) * Math.signum(shooterMotorSetPoint) + "  :  " + shooterMotorSetPoint);
		SmartDashboard.putNumber("Motor Current", shooterTalon.getOutputCurrent());
		shooterTalon.set(shooterMotorSetPoint);
	}

	@Override
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
			setMotorSpeed(SmartDashboard.getNumber("Goal Shooter Speed", 0));
			Timer.delay(0.005); // wait for a motor update time
		}
	}
	
	@Override
	public void disabled() {
		setMotorSpeed(0);
	}
}
