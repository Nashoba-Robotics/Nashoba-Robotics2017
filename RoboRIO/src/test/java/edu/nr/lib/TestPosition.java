package edu.nr.lib;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.nr.lib.Position;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;

public class TestPosition {

	@Test
	public void testOrigin() {
		Position pos = new Position();
		assertEquals(pos.x,0,0);
		assertEquals(pos.y,0,0);
	}
	
	@Test
	public void testInteger() {
		int x = 1;
		int y = 2;
		Position pos = new Position(x,y);
		
		assertEquals(pos.x,x,0);
		assertEquals(pos.y,y,0);
	}
	
	@Test
	public void testSetInteger() {
		int x = 1;
		int y = 2;
		Position pos = new Position();
		pos.setXY(x, y);
		
		assertEquals(pos.x,x,0);
		assertEquals(pos.y,y,0);
	}
	
	@Test
	public void testDouble() {
		double xD = 1.57925;
		double yD = 2.93723;
		
		Position pos = new Position(xD, yD);
		assertEquals(pos.x,xD,0);
		assertEquals(pos.y,yD,0);
	}
	
	@Test
	public void testSetDouble() {
		double xD = 1.57925;
		double yD = 2.93723;
		
		Position pos = new Position();
		pos.setXY(xD, yD);
		assertEquals(pos.x,xD,0);
		assertEquals(pos.y,yD,0);
	}
	
	@Test
	public void testSetPolar() {
		Angle angle = new Angle(2.4 * Math.PI, Angle.Unit.RADIAN);
		double magnitude = 2.93723;
		
		Position pos = new Position();
		pos.setPolar(magnitude, angle);
		assertEquals(pos.getAngle().get(Unit.ROTATION) % 1,angle.get(Unit.ROTATION) % 1,0.0001);
		assertEquals(pos.getMagnitude(),magnitude,0.0001);
		assertEquals(pos.x, 0.907654, 0.000001);
		assertEquals(pos.y, 2.793472, 0.000001);
	}
	
	@Test
	public void testPolar() {
		Angle angle = new Angle(2.4 * Math.PI, Angle.Unit.RADIAN);
		double magnitude = 2.93723;
		
		Position pos = new Position(magnitude, angle);
		assertEquals(pos.getAngle().get(Unit.RADIAN) %( 2*Math.PI),angle.get(Unit.RADIAN) % (2*Math.PI),0.0001);
		assertEquals(pos.getMagnitude(),magnitude,0.0001);
		assertEquals(pos.x, 0.907654, 0.000001);
		assertEquals(pos.y, 2.793472, 0.000001);
	}
	
	@Test
	public void testScaleSingle() {
		double scale = 0.2;
		double x = 5.2;
		double y = 3.7;
		Position pos = new Position(x,y);
		pos.scale(scale);
		assertEquals(pos.x, x * scale, 0.00001);
		assertEquals(pos.y, y * scale, 0.00001);
	}
	
	@Test
	public void testScaleDouble() {
		double xScale = 0.2;
		double yScale = 0.5;
		double x = 5.2;
		double y = 3.7;
		Position pos = new Position(x,y);
		pos.scale(xScale,yScale);
		assertEquals(pos.x, x * xScale, 0.00001);
		assertEquals(pos.y, y * yScale, 0.00001);
	}
	
	@Test
	public void testScalePosition() {
		double xScale = 0.2;
		double yScale = 0.5;
		Position scale = new Position(xScale, yScale);
		double x = 5.2;
		double y = 3.7;
		Position pos = new Position(x,y);
		pos.scale(scale);
		assertEquals(pos.x, x * xScale, 0.00001);
		assertEquals(pos.y, y * yScale, 0.00001);
	}
	
	@Test
	public void testRotate() {
		Angle initialAngle = new Angle(0.2 * Math.PI, Angle.Unit.RADIAN);
		Angle angle = new Angle(0.3 * Math.PI, Angle.Unit.RADIAN);
		double magnitude = 2.93723;
		
		Position pos = new Position(magnitude, initialAngle);
		pos.rotate(angle);
		assertEquals(pos.getAngle().get(Unit.RADIAN),angle.add(initialAngle).get(Unit.RADIAN),0.0001);
		assertEquals(pos.getMagnitude(),magnitude,0.0001);
		assertEquals(pos.x, magnitude * angle.add(initialAngle).cos(), 0.000001);
		assertEquals(pos.y, magnitude * angle.add(initialAngle).sin(), 0.000001);
	}
	
	@Test
	public void testClone() {
		double x = 5.1235;
		double y = 8.12376;
		Position pos = new Position(x,y);
		Position posClone = pos.clone();
		assertEquals(posClone.x, pos.x, 0.00001);
		assertEquals(posClone.y, pos.y, 0.00001);
	}

}