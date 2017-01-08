package edu.nr.lib.interfaces;

public interface DoublePIDOutput {
	  /**
	   * Set the output to the value calculated by PIDController
	   *$
	   * @param output the value calculated by PIDController
	   */
	  public void pidWrite(double outputLeft, double outputRight);

}
