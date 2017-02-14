package edu.nr.robotics;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.SampleRobot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	public CANTalon talonOne;
	public CANTalon talonTwo;
	
	public boolean useJoystick = false;

	private double talonSetpoint = 0;

	public double F = 0.17;
	public double P = 1.5;
	public double I = 0;
	public double D = 0;

	private static final int ticksPerRev = 256;
	
	Joystick stick;

	@Override
	public void robotInit() {
		talonOne = new CANTalon(RobotMap.talon_one);
		talonOne.enableBrakeMode(false);
		talonOne.changeControlMode(TalonControlMode.PercentVbus);
		talonOne.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talonOne.setF(F);
		talonOne.setP(P);
		talonOne.setI(I);
		talonOne.setD(D);
		talonOne.configEncoderCodesPerRev(ticksPerRev);
		
		talonTwo = new CANTalon(RobotMap.talon_two);
		talonTwo.enableBrakeMode(false);
		talonTwo.changeControlMode(TalonControlMode.PercentVbus);
		talonTwo.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talonTwo.setF(F);
		talonTwo.setP(P);
		talonTwo.setI(I);
		talonTwo.setD(D);
		talonTwo.configEncoderCodesPerRev(ticksPerRev);
				
		stick = new Joystick(0);
		
		SmartDashboard.putNumber("Goal Speed", SmartDashboard.getNumber("Goal Speed", 0));	
	}

	public void setMotorSpeed(double speed) {
		talonSetpoint = speed;
		SmartDashboard.putString("Motor One Speed",
				Math.abs(talonOne.getSpeed()) * Math.signum(talonSetpoint) + "  :  " + talonSetpoint);
		SmartDashboard.putNumber("Motor One Current", talonOne.getOutputCurrent());
		talonOne.set(talonSetpoint);

		SmartDashboard.putString("Motor Two Speed",
				Math.abs(talonTwo.getSpeed()) * Math.signum(talonSetpoint) + "  :  " + talonSetpoint);
		SmartDashboard.putNumber("Motor Two Current", talonOne.getOutputCurrent());
		talonTwo.set(talonSetpoint);
}

	@Override
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) { 
			double speed;
			if(useJoystick) {
				double stick_value = stick.getAxis(AxisType.kY);
				speed = stick_value * stick_value * Math.signum(stick_value);
				
				if(Math.abs(speed) < 0.05) {
					speed = 0;
				}
			} else {
				speed = SmartDashboard.getNumber("Goal Speed", 0);
			}
			
			setMotorSpeed(speed);
			Timer.delay(0.005); // wait for a motor update time
		}
	}
	
	@Override
	public void disabled() {
		setMotorSpeed(0);
	}
}
