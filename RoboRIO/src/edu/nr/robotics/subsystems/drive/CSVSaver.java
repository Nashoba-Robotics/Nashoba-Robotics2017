package edu.nr.robotics.subsystems.drive;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Function;

import edu.nr.lib.commandbased.NRSubsystem;

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
	
	private synchronized static <T extends NRSubsystem> void init()  {
		if(singleton == null) {
			ArrayList<Function<Drive, Double>> l = new ArrayList<>();
			l.add(Drive::getEncoderLeftSpeed);
			l.add(Drive::getEncoderRightSpeed);
			singleton = new CSVSaver(l);
		}
	}
	
	public CSVSaver(ArrayList<Function<Drive, Double>> list) {
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
					while(true) {
						while (enabled) {
							out.print(edu.wpi.first.wpilibj.Timer.getFPGATimestamp());
							out.print(",");
							for(Function<Drive, Double> f:list) {
								out.print(f.apply(Drive.getInstance()));
								out.print(",");
							}
							out.print('\n');
							out.flush();
							edu.wpi.first.wpilibj.Timer.delay(0.01);
						}
						edu.wpi.first.wpilibj.Timer.delay(0.1);
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
