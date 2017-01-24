
package edu.nr.robotics;

import edu.nr.lib.DoNothingCommand;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfilerTwoMotor;
import edu.nr.robotics.subsystems.Drive;
import edu.nr.robotics.subsystems.EnableMotionProfile;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public Command driveWall;

	private static Robot singleton;

	public static Robot getInstance() {
		return singleton;
	}


	public SendableChooser<joystick> joystickChooser;

	public enum joystick {
		on, off
	}
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		if (singleton == null)
			singleton = this;
		initSmartDashboard();
		Drive.init();
	}

	public void initSmartDashboard() {
		SmartDashboard.putData("Enable Profiler", new EnableMotionProfile());
		SmartDashboard.putData("Cancel Profiler", new DoNothingCommand(Drive.getInstance()));
	
		joystickChooser = new SendableChooser<joystick>();
		joystickChooser.addDefault("Joystick", joystick.on);
		joystickChooser.addDefault("Motion profiling", joystick.off);
		
		SmartDashboard.putData("Joystick Chooser", joystickChooser);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit() {
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		
		if (Drive.getInstance().talonLB != null) {
			Drive.getInstance().talonLB.setP(Drive.getInstance().turn_P_LEFT);
			Drive.getInstance().talonLB.setI(Drive.getInstance().turn_I_LEFT);
			Drive.getInstance().talonLB.setD(Drive.getInstance().turn_D_LEFT);

			Drive.getInstance().talonRB.setP(Drive.getInstance().turn_P_RIGHT);
			Drive.getInstance().talonRB.setI(Drive.getInstance().turn_I_RIGHT);
			Drive.getInstance().talonRB.setD(Drive.getInstance().turn_D_RIGHT);
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}

	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
		}
	}
}
