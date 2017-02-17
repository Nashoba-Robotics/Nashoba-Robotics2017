package edu.nr.robotics.subsystems.drive;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//TODO: CSVSaver: Make generic
public class CSVSaver {

	boolean enabled = false;
	
	private static CSVSaver singleton;
	
	public static CSVSaver getInstance() {
		if(singleton == null) {
			init();
		}
		return singleton;
	}
	
	private synchronized static void init() {
		if(singleton == null) {
			singleton = new CSVSaver();
		}
	}
	
	public CSVSaver() {
		FileWriter fw;
		PrintWriter out;
		BufferedWriter buffer;

		try {
			fw = new FileWriter("/home/lvuser/MotorSamplesWithWeight.csv", true);
			buffer = new BufferedWriter(fw);
			out = new PrintWriter(buffer);
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (enabled) {
						out.print(edu.wpi.first.wpilibj.Timer.getFPGATimestamp());
						out.print(",");
						out.print(Drive.getInstance().getEncoderLeftSpeed());
						out.print(",");
						out.println(Drive.getInstance().getEncoderRightSpeed());
						out.flush();
						edu.wpi.first.wpilibj.Timer.delay(0.01);
					}
				}
			}).start();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void disable(){
		enabled = false;
	}
	
}
