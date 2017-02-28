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

	private double talonSetpoint1 = 0;
	private double talonSetpoint2 = 0;

	public double F = 0.17;
	public double P = 1.5;
	public double I = 0;
	public double D = 0;

	private static final int quadTicksPerRev = 256;
	private static final int magTicksPerRev = 1024;
	
	Joystick stick;

	@Override
	public void robotInit() {
		talonOne = new CANTalon(RobotMap.talon_one);
		talonOne.enableBrakeMode(false);
		talonOne.changeControlMode(TalonControlMode.PercentVbus);
		//talonOne.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talonOne.setF(F);
		talonOne.setP(P);
		talonOne.setI(I);
		talonOne.setD(D);
		//talonOne.configEncoderCodesPerRev(quadTicksPerRev);
		
		talonTwo = new CANTalon(RobotMap.talon_two);
		talonTwo.enableBrakeMode(false);
		talonTwo.changeControlMode(TalonControlMode.PercentVbus);
		//talonTwo.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		talonTwo.setF(F);
		talonTwo.setP(P);
		talonTwo.setI(I);
		talonTwo.setD(D);
		//talonTwo.configEncoderCodesPerRev(magTicksPerRev);
				
		stick = new Joystick(0);
		SmartDashboard.putNumber("Goal Speed 1", SmartDashboard.getNumber("Goal Speed 1", 0));
		SmartDashboard.putNumber("Goal Speed 2", SmartDashboard.getNumber("Goal Speed 2", 0));
		
	}

	public void setMotorSpeed(double speed1, double speed2) {
		talonSetpoint1 = speed1;
		talonSetpoint2 = speed2;
		SmartDashboard.putString("Motor One Speed",
				Math.abs(talonOne.getSpeed()) * Math.signum(talonSetpoint1) + "  :  " + talonSetpoint1);
		SmartDashboard.putNumber("Motor One Current", talonOne.getOutputCurrent());
		talonOne.set(talonSetpoint1);

		SmartDashboard.putString("Motor Two Speed",
				Math.abs(talonTwo.getSpeed()) * Math.signum(talonSetpoint2) + "  :  " + talonSetpoint2);
		SmartDashboard.putNumber("Motor Two Current", talonOne.getOutputCurrent());
		talonTwo.set(talonSetpoint2);
		
		SmartDashboard.putNumber("Encoder Test", talonTwo.getEncPosition());
}

	@Override
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) { 
			double speed_1;
			double speed_2;
			if(useJoystick) {
				double stick_value = stick.getAxis(AxisType.kY);
				speed_1 = stick_value * stick_value * Math.signum(stick_value);
				speed_2 = stick_value * stick_value * Math.signum(stick_value);

				if(Math.abs(speed_1) < 0.05) {
					speed_1 = 0;
					speed_2 = 0;
				}
			} else {
				speed_1 = SmartDashboard.getNumber("Goal Speed 1", 0);
				speed_2 = SmartDashboard.getNumber("Goal Speed 2", 0);
			}
			
			setMotorSpeed(speed_1, speed_2);
			Timer.delay(0.005); // wait for a motor update time
		}
	}
	
	@Override
	public void disabled() {
		setMotorSpeed(0, 0);
	}
}
