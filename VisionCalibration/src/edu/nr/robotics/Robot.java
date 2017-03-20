package edu.nr.robotics;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;

import edu.nr.lib.network.TCPServer;
import edu.nr.lib.network.TCPServer.NetworkingDataType;
import edu.nr.lib.network.TCPServer.Num;

public class Robot extends IterativeRobot {
	
	static ArrayList<NetworkingDataType> types;
	static ArrayList<NetworkingDataType> gearTypes;
	
	static {
		types = new ArrayList<>();
		types.add(new NetworkingDataType('a', "turret angle"));
		types.add(new NetworkingDataType('d', "turret distance"));
		types.add(new NetworkingDataType('t', "turret timestamp"));
		types.add(new NetworkingDataType('g', "turret rectangles"));
		types.add(new NetworkingDataType('b', "one rect height"));
		types.add(new NetworkingDataType('c', "one rect width"));
		types.add(new NetworkingDataType('e', "one rect x"));
		types.add(new NetworkingDataType('f', "one rect y"));
		types.add(new NetworkingDataType('h', "two rect height"));
		types.add(new NetworkingDataType('i', "two rect width"));
		types.add(new NetworkingDataType('j', "two rect x"));
		types.add(new NetworkingDataType('k', "two rect y"));
		types.add(new NetworkingDataType('l', "real rect bottom"));
		
		gearTypes = new ArrayList<>();
		gearTypes.add(new NetworkingDataType('a', "gear angle"));
		gearTypes.add(new NetworkingDataType('d', "gear distance"));
		gearTypes.add(new NetworkingDataType('t', "gear timestamp"));
		gearTypes.add(new NetworkingDataType('m', "gear mid dist"));
		gearTypes.add(new NetworkingDataType('h', "gear height"));
	}


	@Override
	public void robotInit() {
		
		TCPServer.Num.turret.init(types, TCPServer.defaultPort);
		TCPServer.Num.gear.init(gearTypes, TCPServer.defaultPort + 1);
	}
	
	@Override
	public void robotPeriodic() {
		if(TCPServer.Num.gear.getInstance().isConnected()) {
			int angle = TCPServer.Num.gear.getInstance().getValue("gear angle");
			int distance = TCPServer.Num.gear.getInstance().getValue("gear distance");
			int timestamp = TCPServer.Num.gear.getInstance().getValue("gear timestamp");
			int midDist = TCPServer.Num.gear.getInstance().getValue("gear mid dist");
			double height = (double)TCPServer.Num.gear.getInstance().getValue("gear height");
			double targetAngle;
			if (height > 0) {
				targetAngle = (double)(midDist) / (1.419 * Math.pow(10, -9) * Math.pow(height, 4) - 9.542 * Math.pow(10, -7) * Math.pow(height, 3) + 3.661 * Math.pow(10, -4) * Math.pow(height, 2) + 0.08515 * height + 61.52);
			} else {
				targetAngle = 0;
			}
			SmartDashboard.putNumber("Gear Angle", angle);
			SmartDashboard.putNumber("Gear Distance", distance);
			SmartDashboard.putNumber("Gear Timestamp", timestamp);
			SmartDashboard.putNumber("Target Angle", targetAngle);
			
			System.out.println("Gear Angle: " + angle + "\t Distance: " + distance + "\t Timestamp: " + timestamp + "\t Gear Target Angle: " + targetAngle);
		}

		if(TCPServer.Num.turret.getInstance().isConnected()) {
			int angle = TCPServer.Num.turret.getInstance().getValue("turret angle");
			int distance = TCPServer.Num.turret.getInstance().getValue("turret distance");
			int timestamp = TCPServer.Num.turret.getInstance().getValue("turret timestamp");
			int rectangles = TCPServer.Num.turret.getInstance().getValue("turret rectangles");
			int height1 = TCPServer.Num.turret.getInstance().getValue("one rect height");
			int width1 = TCPServer.Num.turret.getInstance().getValue("one rect width");
			int x1 = TCPServer.Num.turret.getInstance().getValue("one rect x");
			int y1 = TCPServer.Num.turret.getInstance().getValue("one rect y");
			int height2 = TCPServer.Num.turret.getInstance().getValue("two rect height");
			int width2 = TCPServer.Num.turret.getInstance().getValue("two rect width");
			int x2 = TCPServer.Num.turret.getInstance().getValue("two rect x");
			int y2 = TCPServer.Num.turret.getInstance().getValue("two rect y");
			int bottom = TCPServer.Num.turret.getInstance().getValue("real rect bottom");
			SmartDashboard.putNumber("Turret Angle", angle);
			SmartDashboard.putNumber("Turret Distance", distance);
			SmartDashboard.putNumber("Turret Timestamp", timestamp);
			SmartDashboard.putNumber("Turret Rectangles", rectangles);
			System.out.println("Turret Angle: " + angle + "\t Distance: " + distance + "\t Timestamp: " + 
				timestamp + "\t Goodrect Size: " + rectangles + "\t Height 1: "
				+ "\t Rect 1 Height: " + height1 + "\t Rect 1 Width: " + width1 + "\t Rect 1 X: " + x1 + 
				"\t Rect 1 Y: " + y1 + "\t Rect 2 Height" + height2 + "\t Rect 2 Width" + width2 + "\t Rect 2 X: " +
				x2 + "\t Rect 2 Y" + y2 + "\t Real Rect Bottom: " + bottom);
		}
	}
}
