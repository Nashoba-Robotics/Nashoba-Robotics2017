package edu.nr.lib;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SerialPort;

public class NavX {
	
	private static NavX singleton;
	
	public static NavX getInstance() {
		init();
		return singleton;
	}
	
	public static void init() {
		if(singleton == null)
			singleton = new NavX();
	}

	public AHRS ahrs;
	
	public NavX() {
		try {
            /* Communicate w/navX MXP via the MXP SPI Bus.                                     */
            /* Alternatively:  I2C.Port.kMXP, SPI.Port.kMXP or SerialPort.Port.kUSB     */
            /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
            ahrs = new AHRS(I2C.Port.kOnboard); 
        } catch (Exception ex ) {
            System.out.println("Error instantiating navX MXP:  " + ex.getMessage());
        }	
	}

	public double getDisplacementX() {
		return ahrs.getDisplacementX();
	}

	public double getYaw(AngleUnit unit) {
		if(unit == AngleUnit.DEGREE)
			return ahrs.getAngle();
		if(unit == AngleUnit.RADIAN)
			return Math.toRadians(ahrs.getAngle());
		System.err.println("Angle unit: " + unit + " is not known by the NavX getYaw method");
		return 0;
	}
	
	public void reset() {
		ahrs.reset();
		ahrs.resetDisplacement();
	}
	
}
