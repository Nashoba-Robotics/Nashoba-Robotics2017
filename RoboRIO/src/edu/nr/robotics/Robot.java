
package edu.nr.robotics;

import java.util.ArrayList;

import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.network.TCPServer.NetworkingDataType;
import edu.nr.lib.network.TCPServer.Num;
import edu.nr.robotics.auton.CenterGearAndShootAutoCommand;
import edu.nr.robotics.auton.DriveToShooterSideGearAutoCommand;
import edu.nr.robotics.auton.DriveToMiddleGearAutoCommand;
import edu.nr.robotics.auton.DriveToNonShooterSideGearAutoCommand;
import edu.nr.robotics.auton.HopperAndShootAutoCommand;
import edu.nr.robotics.auton.SideGearAndShootAutoCommand;
import edu.nr.robotics.auton.GearHopperAndShootAutoCommand;
import edu.nr.robotics.auton.SideOfField;
import edu.nr.robotics.multicommands.AutoTrackingCalculationCommand;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.agitator.AgitatorRunCommand;
import edu.nr.robotics.subsystems.drive.CSVSaver;
import edu.nr.robotics.subsystems.drive.CSVSaverDisable;
import edu.nr.robotics.subsystems.drive.CSVSaverEnable;
import edu.wpi.first.wpilibj.CameraServer;
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

	Command autonomousCommand;
	SendableChooser<Command> autoChooser = new SendableChooser<>();
	
	public static SideOfField side;
	SendableChooser<SideOfField> sideChooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		CameraServer.getInstance().startAutomaticCapture();

		autoChooserInit();
		tcpServerInit();
		OI.init();
		smartDashboardInit();
	}
	
	/**
	 * Initialize an autonomous mode selector. The selector is sent to SmartDashboard to be set.
	 * 
	 * Set a default choice by calling {@link SendableChooser#addDefault} and set other choices by calling {@link SendableChooser#addObject}
	 */
	public void autoChooserInit() {
		autoChooser.addDefault("Do Nothing", new DoNothingCommand());
		autoChooser.addObject("Right Gear", new DriveToNonShooterSideGearAutoCommand());
		autoChooser.addObject("Center Gear", new DriveToMiddleGearAutoCommand());
		autoChooser.addObject("Left Gear", new DriveToShooterSideGearAutoCommand());
		autoChooser.addObject("Hopper and Shoot", new HopperAndShootAutoCommand());
		autoChooser.addObject("Left Gear and Shoot", new SideGearAndShootAutoCommand());
		autoChooser.addObject("Center Gear and Shoot", new CenterGearAndShootAutoCommand());
		autoChooser.addObject("Left Gear and Hopper Shoot", new GearHopperAndShootAutoCommand());
		SmartDashboard.putData("Auto mode", autoChooser);
		
		sideChooser.addDefault("Red", SideOfField.red);
		sideChooser.addObject("Blue", SideOfField.blue);
		SmartDashboard.putData("Side of field", sideChooser);
	}
	
	public void smartDashboardInit() {
		SmartDashboard.putData(new CSVSaverEnable());
		SmartDashboard.putData(new CSVSaverDisable());
	}
	
	/**
	 * Initialize the {@link TCPServer}
	 */
	public void tcpServerInit() {
		ArrayList<NetworkingDataType> turret_cam_types = new ArrayList<>();
		turret_cam_types.add(new NetworkingDataType('a', "angle")); //TODO: Coprocessor: Get angle sign and units
		turret_cam_types.add(new NetworkingDataType('d', "distance")); //TODO: Coprocessor: Get distance unit
		turret_cam_types.add(new NetworkingDataType('t', "time"));
		Num.turret.init(turret_cam_types, TCPServer.defaultPort);
		
		ArrayList<NetworkingDataType> gear_cam_types = new ArrayList<>();
		gear_cam_types.add(new NetworkingDataType('a', "angle")); //TODO: Coprocessor: Get angle sign and units
		gear_cam_types.add(new NetworkingDataType('d', "distance")); //TODO: Coprocessor: Get distance unit
		gear_cam_types.add(new NetworkingDataType('t', "time"));
		Num.gear.init(gear_cam_types, TCPServer.defaultPort + 1);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	/**
	 * This function is called once each time the robot enters Autonomous mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot begins autonomous and to start the autonomous command.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = autoChooser.getSelected();
		side = sideChooser.getSelected();

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
		
	}

	/**
	 * This function is called once each time the robot enters Teleop mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot begins teleop and to end the autonomous command.
	 */
	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		
		new AutoTrackingCalculationCommand().start();
	}

	/**
	 * This function is called periodically while the robot is disabled
	 */
	@Override
	public void disabledPeriodic() {
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	/**
	 * This function is called periodically during all robot modes
	 */
	@Override
	public void robotPeriodic() {
		Scheduler.getInstance().run();
		
		Periodic.runAll();
		SmartDashboardSource.runAll();
	}
}
