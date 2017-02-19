package edu.nr.lib;
import static org.junit.Assert.*;
import org.junit.*;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Type;

public class TestAngle {

	@Test
	public void testBasicConversion() {
		Angle angleRads = new Angle(2*Math.PI, Angle.Type.RADIAN);
		Angle angleRotations = new Angle(1, Angle.Type.ROTATION);

		Angle angleDegrees = new Angle(360, Angle.Type.DEGREE);

		assertEquals(angleRads.get(Type.DEGREE), angleRotations.get(Type.DEGREE), 0.0001);
		assertEquals(angleRads.get(Type.DEGREE), angleDegrees.get(Type.DEGREE), 0.0001);

	}
	
	@Test
	public void testCosine() {
		Angle angleRads = new Angle(2*Math.PI, Angle.Type.RADIAN);
		assertEquals(angleRads.cos(), 1, 0.0001);
	
	}
}
