package edu.nr.lib;

public class Position {
	//Based off of https://github.com/Team488/SeriouslyCommonLib/blob/master/src/xbot/common/math/XYPair.java
	public static final Position ZERO = new Position();
	
	public double x;
	public double y;

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Position() {
		this(0,0);
	}
	
	public Position setXY(double x, double y) {
		this.x = x;
		this.y = y;
		
		return this;
	}
	
	public Position(double magnitude, double angle, AngleUnit unit) {
		if(unit == AngleUnit.DEGREE)
			angle = Math.toRadians(angle);
        x = magnitude * Math.cos(angle);
        y = magnitude * Math.sin(angle);
	}
	
	/**
	 * Set the coordinates using polar coordinates
	 * @param magnitude
	 * @param angle
	 * @param unit the unit for the coordinates
	 */
	public Position setPolar(double magnitude, double angle, AngleUnit unit) {
		if(unit == AngleUnit.DEGREE)
			angle = Math.toRadians(angle);
		
        x = magnitude * Math.cos(angle);
        y = magnitude * Math.sin(angle);
        
        return this;
	}
	
	public Position scale(double magnitude) {
		x *= magnitude;
		y *= magnitude;
		return this;
	}
	
	public Position scale(double xMagnitude, double yMagnitude) {
		x *= xMagnitude;
		y *= yMagnitude;
		return this;
	}
	
	public Position scale(Position scale) {
		x *= scale.x;
		y *= scale.y;
		
		return scale;
	}
	
	/**
     * Rotates the current coordinates by a given angle.
     * 
     * @param angle the angle to rotate the pair by
     * @param unit the unit of the angle
     * @return the rotated object
     */
    public Position rotate(double angle, AngleUnit unit) {
		if(unit == AngleUnit.DEGREE)
			angle = Math.toRadians(angle);
		
		double mag = getMagnitude();
		double currentAngle = getAngle(AngleUnit.RADIAN);
        x = mag * Math.cos(angle + currentAngle);
        y = mag * Math.sin(angle + currentAngle);

        return this;
    }
    
    /**
     * Gets the current angle
     * @param unit the AngleUnit to return in
     * @return the angle in radians
     */
    public double getAngle(AngleUnit unit) {
        if(unit == AngleUnit.RADIAN) {
        	return Math.atan2(y, x);
        }
    	return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Gets the polar magnitude of the position
     * (The length of the line from 0,0 to the current position)
     * @return the magnitude
     */
    public double getMagnitude() {
        return Math.hypot(x, y);
    }
    
    public Position add(Position pair) {
        x += pair.x;
        y += pair.y;
        return this;
    }
    
    public Position add(double x, double y) {
    	this.x += x;
    	this.y += y;
    	return this;
    }
	
	@Override
	public Position clone() {
		return new Position(x, y);
	}

    @Override
    public String toString() {
        return "(X:" + x + ", Y:" + y + ")";
    }
}
