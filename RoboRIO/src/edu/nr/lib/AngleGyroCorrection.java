package edu.nr.lib;

import edu.nr.lib.interfaces.GyroCorrection;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.nr.lib.NavX;

public class AngleGyroCorrection extends GyroCorrection {

	private double initialAngle;
	double goalAngle;
	NavX navx;
	
	AngleUnit unit;
	
	public AngleGyroCorrection(double angle, NavX navx, AngleUnit unit) {
		if(navx == null) {
			navx = NavX.getInstance();
		}
		this.navx = navx;
		goalAngle = angle;
		this.unit = unit;
		initialAngle = navx.getYaw(unit);
	}
	
	public AngleGyroCorrection(double angle, AngleUnit unit) {
		this(angle, NavX.getInstance(), unit);
	}
	
	public AngleGyroCorrection(AngleUnit unit) {
		this(0, unit);
	}
	
	public AngleGyroCorrection(NavX navx) {
		this(0, navx, AngleUnit.DEGREE);
	}
	
	public AngleGyroCorrection() {
		this(0, AngleUnit.DEGREE);
	}
	
	public AngleGyroCorrection(NavX navx, AngleUnit unit) {
		this(0, navx, unit);
	}

	public double get() {
		return getAngleErrorDegrees();
	}
	
	@Override
	public double getAngleErrorDegrees()
	{
		if(initialized == false)
		{
			reset();
			initialized = true;
		}
		double currentAngle = navx.getYaw(unit);
		
		//System.out.println("goalAngle: " + goalAngle);
		//System.out.println("initialAngle: " + initialAngle);
		//System.out.println("currentAngle: " + currentAngle);
		
		//Error is just based off initial angle
    	return (currentAngle - initialAngle) + goalAngle;
	}
	
	@Override
	public void reset()
	{
		System.out.println("Resetting navx... Current initial angle: " + initialAngle);
		initialAngle = navx.getYaw(unit);
		System.out.println("Final initial angle: " + initialAngle);
	}
}
