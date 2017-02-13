package edu.nr.lib;

import edu.nr.lib.interfaces.GyroCorrection;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.nr.lib.NavX;

public class NavXGyroCorrection extends GyroCorrection implements PIDSource {

	private double initialAngle;
	double goalAngle;
	NavX navx;
	
	PIDSourceType type;

	
	AngleUnit unit;
	
	public NavXGyroCorrection(double angle, NavX navx, AngleUnit unit) {
		if(navx == null) {
			navx = NavX.getInstance();
		}
		this.navx = navx;
		goalAngle = angle;
		this.unit = unit;
		initialAngle = navx.getYaw(unit);
		
		type = PIDSourceType.kDisplacement;
	}
	
	public NavXGyroCorrection(double angle, AngleUnit unit) {
		this(angle, NavX.getInstance(), unit);
	}
	
	public NavXGyroCorrection(AngleUnit unit) {
		this(0, unit);
	}
	
	public NavXGyroCorrection(NavX navx) {
		this(0, navx, AngleUnit.DEGREE);
	}
	
	public NavXGyroCorrection() {
		this(0, AngleUnit.DEGREE);
	}
	
	public NavXGyroCorrection(NavX navx, AngleUnit unit) {
		this(0, navx, unit);
	}

	public double get() {
		return getAngleErrorDegrees();
	}
	


	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		type = pidSource;
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return type;
	}

	@Override
	public double pidGet() {
		return getAngleErrorDegrees();
	}
	
	@Override
	public double getAngleErrorDegrees()
	{
		//Error is just based off initial angle
    	return (navx.getYaw(unit) - initialAngle) + goalAngle;
	}
	
	@Override
	public void reset()
	{
		initialAngle = navx.getYaw(unit);
	}
}
