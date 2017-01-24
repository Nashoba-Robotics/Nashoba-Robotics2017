package edu.nr.lib.interfaces;

import edu.wpi.first.wpilibj.PIDSourceType;

public interface DoublePIDSource {
  /**
   * Set which parameter of the device you are using as a process control
   * variable.
   *
   * @param pidSource
   *            An enum to select the parameter.
   */
  public void setPIDSourceType(PIDSourceType pidSource);

  /**
   * Get which parameter of the device you are using as a process control
   * variable.
   *
   * @return the currently selected PID source parameter
   */
  public PIDSourceType getPIDSourceType();

  /**
   * Get the result to use in PIDController
   *$
   * @return the result to use in PIDController
   */
  public double pidGetLeft();

  /**
   * Get the result to use in PIDController
   *$
   * @return the result to use in PIDController
   */
  public double pidGetRight();
}
