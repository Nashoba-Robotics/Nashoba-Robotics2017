package edu.nr.lib;

import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.sf2.frc.navXSensor;
import com.kauailabs.sf2.orientation.OrientationHistory;

import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Time;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;

public class NavX implements Periodic {
	
	private static NavX singleton;
	
    OrientationHistory orientation_history;
    
    double last_write_timestamp = 0;


	
	public static NavX getInstance() {
		init();
		return singleton;
	}
	
	public synchronized static void init() {
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

	public Angle getYaw() {
		return new Angle(ahrs.getAngle(), Angle.Unit.DEGREE);
	}
	
	/**
	 * 
	 * @param unit What angle unit to use
	 * @param deltaTime how far back to look, in milliseconds. Must be positive.
	 * @return
	 */
	public Angle getHistoricalYaw(Time deltaTime) {
		long dt = (long) deltaTime.get(Time.Unit.MILLISECOND);
        long navx_timestamp = ahrs.getLastSensorTimestamp() - dt;
		return new Angle(orientation_history.getYawDegreesAtTime(navx_timestamp), Angle.Unit.DEGREE);
	}

	@Override
	public void periodic() {
    	//if ((Timer.getFPGATimestamp() - last_write_timestamp) > 5.0) {
    	//	orientation_history.writeToDirectory("/home/lvuser/sf2");
        //  last_write_timestamp = Timer.getFPGATimestamp();
    	//}
   	}

	public void reset() {
		ahrs.reset();
		ahrs.resetDisplacement();
	}
	
}
