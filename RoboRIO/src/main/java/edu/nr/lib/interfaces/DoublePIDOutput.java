package edu.nr.lib.interfaces;

public interface DoublePIDOutput {
	  /**
	   * Set the output to the value calculated by PIDController
	   *$
	   * @param outputLeft the left side value calculated by PIDController
	   * @param outputRight the right side value calculated by PIDController
	   */
	  public void pidWrite(double outputLeft, double outputRight);

}
