
package edu.nr.robotics;

import java.util.ArrayList;

import edu.nr.lib.commandbased.CancelAllCommand;
import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.network.TCPServer.NetworkingDataType;
import edu.nr.lib.network.TCPServer.Num;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.auton.DriveToShooterSideGearAutoCommand;
import edu.nr.robotics.auton.DriveToMiddleGearAutoCommand;
import edu.nr.robotics.auton.DriveToNonShooterSideGearAutoCommand;
import edu.nr.robotics.auton.DriveOverBaselineAutoCommand;
import edu.nr.robotics.auton.DriveToHopperAutoCommand;
import edu.nr.robotics.auton.GearHopperAutoCommand;
import edu.nr.robotics.auton.SideOfField;
import edu.nr.robotics.subsystems.agitator.Agitator;
import edu.nr.robotics.subsystems.drive.CSVSaverDisable;
import edu.nr.robotics.subsystems.drive.CSVSaverEnable;
import edu.nr.robotics.subsystems.intake.Intake;
import edu.nr.robotics.subsystems.loader.Loader;
import edu.nr.robotics.subsystems.shooter.Shooter;
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
	
	public static Robot getInstance() {
		return singleton;
	}
	
	Command autonomousCommand;
	SendableChooser<Command> autoSpotChooser = new SendableChooser<>();
	SendableChooser<Boolean> autoShootChooser = new SendableChooser<>();
	
	public static boolean autoShoot;
	
	public static SideOfField side;
	SendableChooser<SideOfField> sideChooser = new SendableChooser<>();
	
	public static Compressor robotCompressor = new Compressor();
		
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		singleton = this;
		//CameraServer.getInstance().startAutomaticCapture();
		Agitator.init();
		Loader.init();
		Shooter.init();
		Intake.init();

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
		autoSpotChooser.addDefault("Do Nothing", new DoNothingCommand());
		autoSpotChooser.addObject("Baseline", new DriveOverBaselineAutoCommand());
		autoSpotChooser.addObject("Non Shooter Gear", new DriveToNonShooterSideGearAutoCommand());
		autoSpotChooser.addObject("Center Gear", new DriveToMiddleGearAutoCommand());
		autoSpotChooser.addObject("Shooter Gear", new DriveToShooterSideGearAutoCommand());
		autoSpotChooser.addObject("Hopper", new DriveToHopperAutoCommand());
		autoSpotChooser.addObject("Gear and Hopper", new GearHopperAutoCommand());
		SmartDashboard.putData("Auto Destination", autoSpotChooser);
		
		autoShootChooser.addDefault("Shoot", Boolean.TRUE);
		autoShootChooser.addObject("No Shoot", Boolean.FALSE);
		SmartDashboard.putData("Shooting", autoShootChooser);
		
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
		NetworkingDataType turretAngle = new NetworkingDataType('a', "angle", Angle.Unit.DEGREE) {
			public double convert(int in) { //Convert pixels to degrees
				return in; //TODO: Calibration: Turret angle
			}

		};
		NetworkingDataType turretDistance = new NetworkingDataType('d', "distance", Distance.Unit.INCH) {
			public double convert(int in) { //Convert pixels to inches
				return in; //TODO: Calibration: Turret distance
			}

		};
		NetworkingDataType turretTimeStamp = new NetworkingDataType('t', "time", Time.Unit.MILLISECOND);
		turret_cam_types.add(turretAngle);
		turret_cam_types.add(turretDistance);
		turret_cam_types.add(turretTimeStamp);
		Num.turret.init(turret_cam_types, TCPServer.defaultPort);
		
		ArrayList<NetworkingDataType> gear_cam_types = new ArrayList<>();
		NetworkingDataType gearAngle = new NetworkingDataType('a', "angle", Angle.Unit.DEGREE) {
			public double convert(int in) { //Convert pixels to degrees
				return 0.1214662 * in;
			}

		};
		NetworkingDataType gearDistance = new NetworkingDataType('d', "distance", Distance.Unit.INCH) {
			public double convert(int in) { //Convert pixels to inches
				return 5764.4699518042*Math.pow(in, -1.1867971592);
			}

		};
		gear_cam_types.add(gearAngle);
		gear_cam_types.add(gearDistance);
		gear_cam_types.add(new NetworkingDataType('t', "time", Time.Unit.SECOND));
		Num.gear.init(gear_cam_types, TCPServer.defaultPort + 1);
		
		AutoTrackingCalculation.init();
		GearAlignCalculation.init();
		
		turretAngle.addListener(AutoTrackingCalculation.getInstance());
		turretDistance.addListener(AutoTrackingCalculation.getInstance());
		turretTimeStamp.addListener(AutoTrackingCalculation.getInstance());
		
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

	}

	/**
	 * This function is called once each time the robot enters Autonomous mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot begins autonomous and to start the autonomous command.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = autoSpotChooser.getSelected();
		autoShoot = autoShootChooser.getSelected();
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
		//System.out.println("12.56 inches: " + new Distance(1, Distance.Unit.DRIVE_ROTATION).get(Distance.Unit.INCH));
		//System.out.println("60 seconds: " + new Time(1, Time.Unit.MINUTE).get(Time.Unit.SECOND));
		
		Scheduler.getInstance().run();
		
		Periodic.runAll();
		SmartDashboardSource.runAll();
		SmartDashboard.putData(RobotDiagram.getInstance());
	}
}
