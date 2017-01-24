package edu.nr.lib;

import java.util.Timer;
import java.util.TimerTask;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class TalonEncoder extends TimerTask implements PIDSource {

	private final Timer timer;

	//In milliseconds
	private final long period;
	private static final long defaultPeriod = 5; //200 Hz 

	PIDSourceType pidSourceType = PIDSourceType.kDisplacement;
	CANTalon talon;
	double scale = 1; //Divide by this number to make the robot be from -1 to 1 instead of -MAX_RATE to MAX_RATE
	double distancePerRev = 1; 
	int ticksPerRev = 1;
	boolean reverseDirection = false;
	
	boolean monitorAcceleration = false;
	
	double velOfLastTick = 0;
	double timeOfLastTick = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
	double accelOfLastTick = 0;
	
	double pos = 0;
	
	public TalonEncoder(CANTalon talon, boolean monitorAcceleration) {
		this.talon = talon;
		
		this.monitorAcceleration = monitorAcceleration;
		
		this.period = defaultPeriod;
		
		timer = new Timer();
		timer.schedule(this, 0, this.period);
	}
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		pidSourceType = pidSource;
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return pidSourceType;
	}

	@Override
	public double pidGet() {
		if(pidSourceType == PIDSourceType.kDisplacement)
			return getDisplacement();
		if(pidSourceType == PIDSourceType.kRate)
			return getRate();
		System.err.println("PID Source Type " + pidSourceType + "isn't defined by TalonEncoderPIDSource");
		return 0;
	}
	
	public double get() {
		return pidGet();
	}
	
	public double getDisplacement() {
		return (talon.getEncPosition() - pos) * (distancePerRev / ticksPerRev)  * (reverseDirection ? -1 : 1);
	}
	
	public double getRate() {
		return -1 * talon.getEncVelocity() * (distancePerRev / ticksPerRev) / scale * (reverseDirection ? -1 : 1) * 10;//The times 10 is because the enc velocity is per 100 ms
	}

	public double getAccel() {
		return -1 * accelOfLastTick * (distancePerRev / ticksPerRev) / scale * (reverseDirection ? -1 : 1);
	}
	
	public void setReverseDirection(boolean b) {
		reverseDirection = b;
	}
	
	public boolean getReverseDirection() {
		return reverseDirection;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public double getScale() {
		return scale;
	}

	public void setDistancePerRev(double distancePerRev) {
		this.distancePerRev = distancePerRev;
	}
	
	public double getDistancePerRev() {
		return distancePerRev;
	}

	public void setTicksPerRev(int ticksPerRev) {
		this.ticksPerRev = ticksPerRev;
	}
	
	public int getTicksPerRev() {
		return ticksPerRev;
	}

	public void reset() {
		pos = talon.getEncPosition();
	}

	/**
	 * Still uses the distance per rev and ticks per rev and reverse direction
	 * @return rate
	 */
	public double getRateWithoutScaling() {
		return talon.getEncVelocity() * (distancePerRev / ticksPerRev) * (reverseDirection ? -1 : 1) * 10;
	}
	
	/**
	 * Still uses the distance per rev and ticks per rev and reverse direction
	 * @return acceleration
	 */
	public double getAccelWithoutScaling() {
		return accelOfLastTick * (distancePerRev / ticksPerRev) * (reverseDirection ? -1 : 1);
	}

	@Override
	public void run() {
		if(monitorAcceleration) {
			double timeOfThisTick = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
			
			double currentVel = talon.getEncVelocity() * 10; //The times 10 is because the enc velocity is per 100 ms
			
			accelOfLastTick = (currentVel - velOfLastTick) / (timeOfThisTick - timeOfLastTick);
			
			timeOfLastTick = timeOfThisTick;
			velOfLastTick = currentVel;
		}
	}

}
