package edu.nr.lib.interfaces;

import java.util.ArrayList;

public interface Periodic {
	
	public final static ArrayList<Periodic> periodics = new ArrayList<>();
	
	public static void runAll() {
		periodics.forEach(Periodic::periodic);
	}
	
	/**
	 * This function is called every loop of the code
	 */
	public abstract void periodic();

}
