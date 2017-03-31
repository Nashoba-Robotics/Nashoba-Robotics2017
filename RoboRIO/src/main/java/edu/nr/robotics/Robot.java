
package edu.nr.robotics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.nr.lib.GyroCorrection;
import edu.nr.lib.NavX;
import edu.nr.lib.commandbased.CancelAllCommand;
import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.network.TCPServer.NetworkingDataType;
import edu.nr.lib.network.TCPServer.Num;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.auton.DriveOverBaselineAutoCommand;
import edu.nr.robotics.auton.DriveToBlueLeftSideGearAutoCommand;
import edu.nr.robotics.auton.DriveToBlueRightSideGearAutoCommand;
import edu.nr.robotics.auton.DriveToHopperBlueAutoCommand;
import edu.nr.robotics.auton.DriveToHopperRedAutoCommand;
import edu.nr.robotics.auton.DriveToRedLeftSideGearAutoCommand;
import edu.nr.robotics.auton.DriveToMiddleGearAutoCommand;
import edu.nr.robotics.auton.DriveToRedRightSideGearAutoCommand;
import edu.nr.robotics.auton.ShootThenBaselineAuto;
import edu.nr.robotics.auton.SideOfField;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.agitator.Agitator;
import edu.nr.robotics.subsystems.drive.CSVSaverDisable;
import edu.nr.robotics.subsystems.drive.CSVSaverEnable;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.drive.DriveStationaryPIDCommand;
import edu.nr.robotics.subsystems.drive.DriveTurnConstantSmartDashboardSpeedCommand;
import edu.nr.robotics.subsystems.intake.Intake;
import edu.nr.robotics.subsystems.loader.Loader;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.shooter.ShooterSmartDashboardSpeedCommand;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
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

	private static Robot singleton;
	
	public synchronized static Robot getInstance() {
		return singleton;
	}
	
	Command autonomousCommand;
	SendableChooser<Command> autoSpotChooser = new SendableChooser<>();
	
	public static Compressor robotCompressor;
		
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		if(EnabledSubsystems.COMPRESSOR_ENABLED) {
			robotCompressor = new Compressor();
			robotCompressor.stop();
		}
		singleton = this;
		Agitator.init();
		Loader.init();
		Shooter.init();
		Intake.init();

		autoChooserInit();
		tcpServerInit();
		OI.init();
		smartDashboardInit();
		
		cameraInit();
	}
	
	public void cameraInit() {
		//CameraServer.getInstance().startAutomaticCapture();
	}
	
	/**
	 * Initialize an autonomous mode selector. The selector is sent to SmartDashboard to be set.
	 * 
	 * Set a default choice by calling {@link SendableChooser#addDefault} and set other choices by calling {@link SendableChooser#addObject}
	 */
	public void autoChooserInit() {
		if(robotCompressor != null) {
			robotCompressor.start();
		}
		autoSpotChooser.addDefault("Do Nothing", new DoNothingCommand());
		autoSpotChooser.addObject("Baseline", new DriveOverBaselineAutoCommand());
		autoSpotChooser.addObject("Red Left Gear", new DriveToRedLeftSideGearAutoCommand());
		autoSpotChooser.addObject("Blue Left Gear", new DriveToBlueLeftSideGearAutoCommand());
		autoSpotChooser.addObject("Center Gear", new DriveToMiddleGearAutoCommand());
		autoSpotChooser.addObject("Red Right Gear", new DriveToRedRightSideGearAutoCommand());
		autoSpotChooser.addObject("Blue Right Gear", new DriveToBlueRightSideGearAutoCommand());
		autoSpotChooser.addObject("Hopper Red", new DriveToHopperRedAutoCommand());
		autoSpotChooser.addObject("Hopper Blue", new DriveToHopperBlueAutoCommand());
		//autoSpotChooser.addObject("Gear and Hopper", new GearHopperAutoCommand());
		autoSpotChooser.addObject("Shoot then Baseline", new ShootThenBaselineAuto());
		SmartDashboard.putData("Auto Destination", autoSpotChooser);
	}
	
	public void smartDashboardInit() {
		SmartDashboard.putData(new CSVSaverEnable());
		SmartDashboard.putData(new CSVSaverDisable());
		
		SmartDashboard.putData(new DriveStationaryPIDCommand(1, GyroCorrection.DEFAULT_KP_THETA));
		
		SmartDashboard.putData(new DriveTurnConstantSmartDashboardSpeedCommand());
	
		SmartDashboard.putData(new DriveForwardProfilingCommand(Distance.ZERO));

		SmartDashboard.putData(new ShooterSmartDashboardSpeedCommand());
	}
	
	/**
	 * Initialize the {@link TCPServer}
	 */
	public void tcpServerInit() {
		ArrayList<NetworkingDataType> turret_cam_types = new ArrayList<>();
		NetworkingDataType turretAngle = new NetworkingDataType('a', "angle", Angle.Unit.RADIAN) {
			public double convert(int in) { //Convert pixels to radians
				return Math.asin(in/235.95426680376644)/4.298829992446103;

			}

		};
		NetworkingDataType turretDistance = new NetworkingDataType('d', "distance", Distance.Unit.INCH) {
			public double convert(int in) { //Convert pixels to inches
				//return 1.419 * Math.pow(10, -9) * Math.pow(in, 4) - 9.542 * Math.pow(10, -7) * Math.pow(in, 3) + 3.661 * Math.pow(10, -4) * Math.pow(in, 2) + 0.08515 * in + 61.52;
				//return 40.8 + Math.pow(248.4, 0.00404 * in);
				return -80.79309 * (Math.log((double) in) / Math.log(Math.E)) + 567.68835; 
			}

		};
		NetworkingDataType turretTimeStamp = new NetworkingDataType('t', "time", Time.Unit.MILLISECOND);
		turret_cam_types.add(turretAngle);
		turret_cam_types.add(turretDistance);
		turret_cam_types.add(turretTimeStamp);
		turret_cam_types.add(new NetworkingDataType('x', "heartbeat", Time.Unit.SECOND));
		Num.turret.init(turret_cam_types, TCPServer.defaultPort+2);
		
		ArrayList<NetworkingDataType> gear_cam_types = new ArrayList<>();
		NetworkingDataType gearAngle = new NetworkingDataType('a', "angle", Angle.Unit.DEGREE) {
			public double convert(int in) { //Convert pixels to degrees
				return 0.1214662 * in;
			}

		};
		NetworkingDataType gearDistance = new NetworkingDataType('d', "distance", Distance.Unit.INCH) {
			public double convert(int in) { //Convert pixels to inches
				return GearDistanceCalibration.get(in);
			}

		};
		gear_cam_types.add(gearAngle);
		gear_cam_types.add(gearDistance);
		gear_cam_types.add(new NetworkingDataType('t', "time", Time.Unit.SECOND));
		gear_cam_types.add(new NetworkingDataType('x', "heartbeat", Time.Unit.SECOND));
		Num.gear.init(gear_cam_types, TCPServer.defaultPort + 1);
		
		AutoTrackingCalculation.init();
		StationaryTrackingCalculation.init();
		GearAlignCalculation.init();
		
		turretAngle.addListener(AutoTrackingCalculation.getInstance());
		turretDistance.addListener(AutoTrackingCalculation.getInstance());
		turretTimeStamp.addListener(AutoTrackingCalculation.getInstance());
		
		turretAngle.addListener(StationaryTrackingCalculation.getInstance());
		turretDistance.addListener(StationaryTrackingCalculation.getInstance());
		turretTimeStamp.addListener(StationaryTrackingCalculation.getInstance());
		
		gearAngle.addListener(GearAlignCalculation.getInstance());
		gearDistance.addListener(GearAlignCalculation.getInstance());

	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		for (NRSubsystem subsystem : NRSubsystem.subsystems) {
			subsystem.disable();
		}

	}

	/**
	 * This function is called once each time the robot enters Autonomous mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot begins autonomous and to start the autonomous command.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = autoSpotChooser.getSelected();

		System.out.println("Initializing auto command: " + autonomousCommand);
		
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
		new CancelAllCommand().start();
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
		SmartDashboard.putNumber("Gear Angle", GearAlignCalculation.getInstance().getAngleToTurn().get(Angle.Unit.DEGREE));
		SmartDashboard.putNumber("Turret Angle", AutoTrackingCalculation.getInstance().getRawTurretAngle().get(Angle.Unit.DEGREE));
		if(robotCompressor != null) {
			SmartDashboard.putBoolean("Compressor", Robot.robotCompressor.getClosedLoopControl());
		}
		
		SmartDashboard.putNumber("Sine of time", Math.sin(edu.wpi.first.wpilibj.Timer.getFPGATimestamp()));
		
		SmartDashboard.putData(RobotDiagram.getInstance());
	}
}
