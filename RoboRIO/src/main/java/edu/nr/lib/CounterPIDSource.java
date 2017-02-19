package edu.nr.lib;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class CounterPIDSource extends Counter implements PIDSource {
	
	PIDSourceType PIDtype = PIDSourceType.kRate;
	
	double scale = 1;
	
	boolean inverted = false;
	
	public CounterPIDSource(int port) {
		this(port, 1);
	}
	
	public CounterPIDSource(int port, double distancePerPulse) {
		super(port);
		setDistancePerPulse(distancePerPulse);
	}
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		PIDtype = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDtype;
	}

	@Override
	public double pidGet() {
		if(PIDtype == PIDSourceType.kRate)
			return getRate()/scale * (inverted ? -1 : 1);
		return getDistance();
	}

	public void scale(double scaleVal) {
		scale = scaleVal;
	}

	public void setInverted() {
		inverted = true;
	}
}
