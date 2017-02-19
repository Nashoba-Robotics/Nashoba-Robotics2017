package edu.nr.lib;
import static org.junit.Assert.*;
import org.junit.*;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;

public class TestAngle {

	@Test
	public void testBasicConversion() {
		Angle angleRads = new Angle(2*Math.PI, Angle.Unit.RADIAN);
		Angle angleRotations = new Angle(1, Angle.Unit.ROTATION);

		Angle angleDegrees = new Angle(360, Angle.Unit.DEGREE);

		assertEquals(angleRads.get(Unit.DEGREE), angleRotations.get(Unit.DEGREE), 0.0001);
		assertEquals(angleRads.get(Unit.DEGREE), angleDegrees.get(Unit.DEGREE), 0.0001);

	}
	
	@Test
	public void testCosine() {
		Angle angleRads = new Angle(2*Math.PI, Angle.Unit.RADIAN);
		assertEquals(angleRads.cos(), 1, 0.0001);
	
	}
}
