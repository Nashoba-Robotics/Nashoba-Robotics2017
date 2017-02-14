package edu.nr.lib.interfaces;

import java.util.ArrayList;

/**
 * An interface for code to implement if they want to be called every loop of the code.
 * 
 * After implementing, they should add themselves to {@link Periodic#periodics} to actually be called.
 */
public interface Periodic {
	
	/**
	 * A list of all the classes that should be called every loop of the code.
	 */
	public final static ArrayList<Periodic> periodics = new ArrayList<>();
	
	/**
	 * Runs every periodic class. This should only be called in one place in the code!
	 */
	public static void runAll() {
		periodics.forEach(Periodic::periodic);
	}
	
	/**
	 * This function is called every loop of the code
	 */
	public abstract void periodic();

}
