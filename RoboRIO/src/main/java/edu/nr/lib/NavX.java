package edu.nr.lib;

import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.sf2.frc.navXSensor;
import com.kauailabs.sf2.orientation.OrientationHistory;

import edu.nr.lib.interfaces.Periodic;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;

public class NavX implements Periodic {
	
	private static NavX singleton;
	
    OrientationHistory orientation_history;
    
    double last_write_timestamp = 0;


	
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
            ahrs = new AHRS(SerialPort.Port.kMXP); 
            navXSensor navx_sensor = new navXSensor(ahrs, "Drivetrain Orientation");
            orientation_history = new OrientationHistory(navx_sensor,
        		ahrs.getRequestedUpdateRate() * 10);
            
            periodics.add(this);

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
	
	/**
	 * 
	 * @param unit What angle unit to use
	 * @param deltaTime how far back to look, in milliseconds. Must be positive.
	 * @return
	 */
	public double getHistoricalYaw(AngleUnit unit, long deltaTime) {
        long navx_timestamp = ahrs.getLastSensorTimestamp() - deltaTime;
		if(unit == AngleUnit.DEGREE)
			return orientation_history.getYawDegreesAtTime(navx_timestamp);
		if(unit == AngleUnit.RADIAN)
			return Math.toRadians(orientation_history.getYawDegreesAtTime(navx_timestamp));
		System.err.println("Angle unit: " + unit + " is not known by the NavX getYaw method");
		return 0;
	}

	@Override
	public void periodic() {
    	if ((Timer.getFPGATimestamp() - last_write_timestamp) > 5.0) {
    		orientation_history.writeToDirectory("/home/lvuser/sf2");
            last_write_timestamp = Timer.getFPGATimestamp();
    	}
   	}

	public void reset() {
		ahrs.reset();
		ahrs.resetDisplacement();
	}
	
}
