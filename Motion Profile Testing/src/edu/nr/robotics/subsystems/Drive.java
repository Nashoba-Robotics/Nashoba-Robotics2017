package edu.nr.robotics.subsystems;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.StatusFrameRate;
import com.ctre.CANTalon.TalonControlMode;

import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.NRMath;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.NavX;
import edu.nr.lib.interfaces.DoublePIDOutput;
import edu.nr.lib.interfaces.DoublePIDSource;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfilerTwoMotor;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectorySimple;
import edu.nr.lib.motionprofiling.TwoDimensionalMotionProfilerPathfinder;
import edu.nr.lib.motionprofiling.TwoDimensionalMotionProfilerPathfinderModified;
import edu.nr.lib.sensorhistory.HistoricalCANTalon;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;
import edu.nr.lib.sensorhistory.TalonEncoder;
import edu.nr.lib.units.Angle;

public class Drive extends NRSubsystem implements DoublePIDOutput, DoublePIDSource {

	public static boolean driveEnabled = true;

	public boolean running = false;

	TwoDimensionalMotionProfilerPathfinder profiler;

	private static Drive singleton;

	public CANTalon talonLF;
	public CANTalon talonRF;
	public CANTalon talonLB;
	public CANTalon talonRB;
	
	public TalonEncoder encRight;
	public TalonEncoder encLeft;

	public double leftMotorSetPoint = 0;
	public double rightMotorSetPoint = 0;

	public final double turn_F_LEFT = 0.873;
	public final double turn_F_RIGHT = 0.966;
	public final double turn_P_LEFT = 0; //2.5 when on ground
	public final double turn_I_LEFT = 0;
	public final double turn_D_LEFT = 0;
	public final double turn_P_RIGHT = 0; //2.5 when on ground
	public final double turn_I_RIGHT = 0;
	public final double turn_D_RIGHT = 0;

	public static final int ticksPerRev = 256;

	PIDSourceType type = PIDSourceType.kRate;

	// public static final double ka = 0.01, kp = 0.5, kd = 0.0, kv = 1 /
	// (RobotMap.MAX_RPS * RobotMap.WHEEL_DIAMETER * Math.PI * 0.0254), kp_theta
	// = 0.075;

	// FOR TWO DIMENSIONAL
	public static double ka = 0.015, kp = 1.6,
			ki = 0, kd = 0.2, kp_theta = 0.037,
			kv = 1 / (RobotMap.MAX_RPS * RobotMap.WHEEL_DIAMETER * Math.PI * (1 / 39.37));

	// FOR ONE DIMENSIONAL
	// public static final double ka = 0.01, kp = 0.0, kd = 0.0105, kv = 1 /
	// (RobotMap.MAX_RPS * RobotMap.WHEEL_DIAMETER * Math.PI * 0.0254), kp_theta
	// = 0.00035;

	private Drive() throws IOException {
		if (driveEnabled) {
			talonLB = new CANTalon(RobotMap.talonLB);
			talonLB.enableBrakeMode(true);
			talonLB.changeControlMode(TalonControlMode.Speed);
			talonLB.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			talonLB.setF(turn_F_LEFT);
			talonLB.setP(turn_P_LEFT);
			talonLB.setI(turn_I_LEFT);
			talonLB.setD(turn_D_LEFT);
			talonLB.configEncoderCodesPerRev(ticksPerRev);
			talonLB.reverseSensor(true);
			talonLB.setStatusFrameRateMs(StatusFrameRate.Feedback, 1);

			talonRB = new CANTalon(RobotMap.talonRB);
			talonRB.enableBrakeMode(true);
			talonRB.changeControlMode(TalonControlMode.Speed);
			talonRB.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			talonRB.setF(turn_F_RIGHT);
			talonRB.setP(turn_P_RIGHT);
			talonRB.setI(turn_I_RIGHT);
			talonRB.setD(turn_D_RIGHT);
			talonRB.configEncoderCodesPerRev(ticksPerRev);
			talonRB.reverseSensor(false);
			talonRB.setStatusFrameRateMs(StatusFrameRate.Feedback, 1);

			talonLF = new CANTalon(RobotMap.talonLF);
			talonLF.enableBrakeMode(true);
			talonLF.changeControlMode(TalonControlMode.Follower);
			talonLF.set(talonLB.getDeviceID());

			talonRF = new CANTalon(RobotMap.talonRF);
			talonRF.enableBrakeMode(true);
			talonRF.changeControlMode(TalonControlMode.Follower);
			talonRF.set(talonRB.getDeviceID());

			profiler = new TwoDimensionalMotionProfilerPathfinder(this, this, kv, ka, kp, ki, kd, kp_theta,
					RobotMap.MAX_RPS * RobotMap.WHEEL_DIAMETER * Math.PI * 0.0254 * 0.5,
					RobotMap.MAX_ACC * RobotMap.WHEEL_DIAMETER * Math.PI * 0.0254 * 0.5, 
					RobotMap.MAX_JERK * RobotMap.WHEEL_DIAMETER * Math.PI * 0.0254 * 0.5,
					ticksPerRev, RobotMap.WHEEL_DIAMETER * 0.0254, RobotMap.WHEEL_BASE, true);
			
			encLeft = new TalonEncoder(talonLB);
			encRight = new TalonEncoder(talonRB);
			
			SmartDashboard.putNumber("ka", ka);
			SmartDashboard.putNumber("kv", kv);
			SmartDashboard.putNumber("kd", kd);
			SmartDashboard.putNumber("ki", ki);
			SmartDashboard.putNumber("kp", kp);
			SmartDashboard.putNumber("kp_theta", kp_theta);

			SmartDashboard.putNumber("X Waypoint 1", 2.5);
			SmartDashboard.putNumber("X Waypoint 2", 5);
			SmartDashboard.putNumber("Y Waypoint 1", 1.5);
			SmartDashboard.putNumber("Y Waypoint 2", 3);
			SmartDashboard.putNumber("End Angle 1", 45);
			SmartDashboard.putNumber("End Angle 2", 0);
			setJoystickCommand(new DriveJoystickCommand());
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						//SmartDashboard.putNumber("rightRPM", talonRB.getSpeed());
						//SmartDashboard.putNumber("leftRPM", -talonLB.getSpeed());
						//SmartDashboard.putNumber("rightPos", talonRB.getPosition());
						//SmartDashboard.putNumber("leftPos", -talonLB.getPosition());
	
						SmartDashboard.putNumber("NavX Yaw", NavX.getInstance().getYaw().get(Angle.Unit.DEGREE));
	
						//SmartDashboard.putString("Speed Right 2", Drive.getInstance().talonRB.getSpeed() + " : " +
						//-Drive.getInstance().rightMotorSetPoint *
						//RobotMap.MAX_RPS * 60);
						//SmartDashboard.putString("Speed Left 2",
						//Drive.getInstance().talonLB.getSpeed() + " : " +
						//-Drive.getInstance().leftMotorSetPoint *
						//RobotMap.MAX_RPS * 60);
						
						int deltaTime = 1000;
						SmartDashboard.putString("Current Position L", encLeft.getPosition(deltaTime) + " : " + talonLB.getPosition());
						SmartDashboard.putString("Current Position R", encRight.getPosition(deltaTime) + " : " + talonRB.getPosition());
						
						SmartDashboard.putString("Speed Right 2", Drive.getInstance().talonRB.getSpeed() + " : " + Drive.getInstance().talonRB.getSetpoint());
						SmartDashboard.putString("Speed Left 2", Drive.getInstance().talonLB.getSpeed() + " : " + Drive.getInstance().talonLB.getSetpoint());
	
						// SmartDashboard.putString("Talon Position",
						// (Drive.getInstance().pidGetLeft()) / 3.581 + ":" +
						// -(Drive.getInstance().pidGetRight() -
						// Drive.getInstance().profiler.initialPositionRight) /
						// 3.581 );
	
						//SmartDashboard.putString("Current",
								//talonLB.getOutputCurrent() + " : " + talonLF.getOutputCurrent() + " : " + talonRF.getOutputCurrent() + " : " + talonRB.getOutputCurrent());
	
						try {
							java.util.concurrent.TimeUnit.MILLISECONDS.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

			
			FileWriter fw;
			PrintWriter out;
			BufferedWriter buffer;

			try {
				fw = new FileWriter("/home/lvuser/MotorSamplesWithWeight.csv", true);
				buffer = new BufferedWriter(fw);
				out = new PrintWriter(buffer);
				new Thread(new Runnable() {
					@Override
					public void run() {
						while (running) {
							out.print(edu.wpi.first.wpilibj.Timer.getFPGATimestamp());
							out.print(",");
							out.print(Drive.getInstance().talonLB.getSpeed());
							out.print(",");
							out.println(Drive.getInstance().talonRB.getSpeed());
							out.flush();
							edu.wpi.first.wpilibj.Timer.delay(0.01);
						}
					}
				}).start();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}

	public static Drive getInstance() {
		init();
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			try {
				singleton = new Drive();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets left and right motor speeds to the speeds needed for the given move
	 * and turn values, multiplied by the OI speed multiplier if the speed
	 * multiplier parameter is true. If you don't care about the speed
	 * multiplier parameter, you might want to use {@link arcadeDrive(double
	 * move, double turn)}
	 * 
	 * @param move
	 *            The speed, from -1 to 1 (inclusive), that the robot should go
	 *            at. 1 is max forward, 0 is stopped, -1 is max backward
	 * @param turn
	 *            The speed, from -1 to 1 (inclusive), that the robot should
	 *            turn at. 1 is max right, 0 is stopped, -1 is max left
	 * @param speedMultiplier
	 *            whether or not to use the OI speed multiplier It should really
	 *            only be used for operator driving
	 * 
	 */
	public void arcadeDrive(double move, double turn, boolean speedMultiplier) {
		move = NRMath.limit(move);
		turn = NRMath.limit(turn);
		double leftMotorSpeed, rightMotorSpeed;
		rightMotorSpeed = leftMotorSpeed = move;
		leftMotorSpeed += turn;
		rightMotorSpeed -= turn;

		if (move > 0.0) {
			if (turn > 0.0) {
				leftMotorSpeed = move - turn;
				rightMotorSpeed = Math.max(move, turn);
			} else {
				leftMotorSpeed = Math.max(move, -turn);
				rightMotorSpeed = move + turn;
			}
		} else {
			if (turn > 0.0) {
				leftMotorSpeed = -Math.max(-move, turn);
				rightMotorSpeed = move + turn;
			} else {
				leftMotorSpeed = move - turn;
				rightMotorSpeed = -Math.max(-move, -turn);
			}
		}
		setMotorSpeed(leftMotorSpeed, rightMotorSpeed);
	}

	public void setMotorSpeed(double left, double right) {
		leftMotorSetPoint = -left;
		rightMotorSetPoint = right;
		switch (Robot.getInstance().joystickChooser.getSelected()) {
		case off:
			pidWrite(leftMotorSetPoint, rightMotorSetPoint);
			break;
		case on:
			if (talonLB.getControlMode() == TalonControlMode.Speed)
				talonLB.set(leftMotorSetPoint * RobotMap.MAX_RPS * 60);
			else
				talonLB.set(leftMotorSetPoint);
			if (talonRB.getControlMode() == TalonControlMode.Speed)
				talonRB.set(rightMotorSetPoint * RobotMap.MAX_RPS * 60);
			else
				talonRB.set(rightMotorSetPoint);
			break;
		}
	}
	
	@Override
	public void disable() {
		Drive.getInstance().setMotorSpeed(0, 0);
		Command c = getCurrentCommand();
		if (c != null) {
			c.cancel();
		}
		disableProfiler();

		running = false;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		type = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return type;
	}

	@Override
	public double pidGetLeft() {
		if (type == PIDSourceType.kRate) {
			return -talonLB.getSpeed() / 60;
		} else {
			return talonLB.getPosition();
		}

	}

	@Override
	public double pidGetRight() {
		if (type == PIDSourceType.kRate) {
			return -talonRB.getSpeed() / 60;
		} else {
			return talonRB.getPosition();
		}
	}

	public void enableProfiler() {
		profiler.setKA(SmartDashboard.getNumber("ka", 0));
		profiler.setKP(SmartDashboard.getNumber("kp", 0));
		profiler.setKI(SmartDashboard.getNumber("ki", 0));
		profiler.setKD(SmartDashboard.getNumber("kd", 0));
		profiler.setKV(SmartDashboard.getNumber("kv", 0));
		profiler.setKP_theta(SmartDashboard.getNumber("kp_theta", 0));

		Waypoint[] points = new Waypoint[] {

				//Field waypoint on side would be (2.945, +-(1.868++), -45)
				
				new Waypoint(0, 0, 0),
				//new Waypoint(0.25, 0, 0),
				new Waypoint(SmartDashboard.getNumber("X Waypoint 1", 2.5), SmartDashboard.getNumber("Y Waypoint 1", 1.5),
						Pathfinder.d2r(SmartDashboard.getNumber("End Angle 1", 45))),
				new Waypoint(SmartDashboard.getNumber("X Waypoint 2", 5), SmartDashboard.getNumber("Y Waypoint 2", 3),
						Pathfinder.d2r(SmartDashboard.getNumber("End Angle 2", 0)))
				//new Waypoint(2.5, 1.75, Pathfinder.d2r(60)),
				//new Waypoint(4.75, 3.5, 0),
				//new Waypoint(5.0, 3.5, 0)
		};
		
		profiler.setTrajectory(points);
		profiler.enable();
	}

	public void disableProfiler() {
		profiler.disable();
	}

	public boolean isProfilerEnabled() {
		return profiler.isEnabled();
	}

	@Override
	public void pidWrite(double left, double right) {
		leftMotorSetPoint = -left;
		rightMotorSetPoint = right;

		// System.out.println("Left: " + left + " right: " + right);

		talonLB.set(left * RobotMap.MAX_RPS * 60);
		talonRB.set(right * RobotMap.MAX_RPS * 60);
	}

	@Override
	public void smartDashboardInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void periodic() {
		// TODO Auto-generated method stub
		
	}
}