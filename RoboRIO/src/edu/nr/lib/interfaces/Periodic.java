package edu.nr.lib.interfaces;

import java.util.ArrayList;

public interface Periodic {
	
	public static ArrayList<Periodic> periodics = new ArrayList<>();
	
	public abstract void periodic();

}
