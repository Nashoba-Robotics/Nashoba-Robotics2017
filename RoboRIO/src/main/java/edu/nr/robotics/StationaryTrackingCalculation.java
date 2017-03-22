package edu.nr.robotics;

import edu.nr.lib.NRMath;
import edu.nr.lib.Units;
import edu.nr.lib.network.NetworkingDataTypeListener;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StationaryTrackingCalculation implements NetworkingDataTypeListener {
		
	Angle turretAngle = Angle.ZERO;
	Angle deltaTurretAngle = Angle.ZERO;
	
	Angle hoodAngle = Angle.ZERO;
	
	AngularSpeed shooterSpeed = AngularSpeed.ZERO;
	
	private Time lastSeenTimeStamp = Time.ZERO;
	private Angle lastSeenAngle = Angle.ZERO;
	private Distance lastSeenDistance = Distance.ZERO;
	
	private Time timeOfLastData = Time.ZERO;
	
	private static StationaryTrackingCalculation singleton;
	
	public synchronized static void init() {
		if(singleton == null) {
			singleton = new StationaryTrackingCalculation();
		}
	}
	
	public static StationaryTrackingCalculation getInstance() {
		if(singleton == null) {
			init();
		}
		return singleton;
	}
	
	@Override
	public void updateDataType(TCPServer.NetworkingDataType type, double value) {
		if(type.identifier == 'a') {
			lastSeenAngle = new Angle(value, (Angle.Unit) type.unit);
		} else if(type.identifier == 'd') {
			lastSeenDistance = new Distance(value, (Distance.Unit) type.unit);
		} else if(type.identifier == 't') {
			lastSeenTimeStamp = new Time(value, (Time.Unit) type.unit);
		}
		timeOfLastData = Time.getCurrentTime();
		
		Distance distReal = NRMath.lawOfCos(lastSeenDistance, RobotMap.Y_CAMERA_OFFSET, lastSeenAngle);
		
		SmartDashboard.putNumber("Turret Distance", distReal.get(Distance.Unit.INCH));
		
		deltaTurretAngle = (Units.HALF_CIRCLE.sub(NRMath.lawOfCos(distReal, RobotMap.Y_CAMERA_OFFSET, lastSeenDistance))).mul(lastSeenAngle.getSign());

		SmartDashboard.putNumber("Delta Turret Angle", deltaTurretAngle.get(Angle.Unit.DEGREE));

		turretAngle = Turret.getInstance().getPosition().add(deltaTurretAngle);
		
		//System.out.println("Last Seen Angle: " + lastSeenAngle.get(Angle.Unit.DEGREE));
		//System.out.println("Delta Turret Angle: " + deltaTurretAngle.get(Angle.Unit.DEGREE));
		
		//System.out.println("Turret delta angle to set: " + turretAngle.get(Angle.Unit.DEGREE));
		//System.out.println("Distance: " + distReal.get(Distance.Unit.INCH));

		hoodAngle = Hood.getInstance().getPosition().add(Calibration.getHoodAngleFromDistance(distReal));
		
		//System.out.println("Hood delta angle to set: " + hoodAngle.get(Angle.Unit.DEGREE));


		shooterSpeed = Calibration.getShooterSpeedFromDistance(distReal);

	}
	
	/**
	 * @return Turret angle in degrees
	 */
	public Angle getTurretAngle() {
		return turretAngle;
	}
	
	/**
	 * @return Delta turret angle in degrees
	 */
	public Angle getDeltaTurretAngle() {
		return deltaTurretAngle;
	}

	/**
	 * @return Hood angle in degrees
	 */
	public Angle getHoodAngle() {
		return hoodAngle;
	}
	
	/**
	 * @return Shooter speed in rpm
	 */
	public AngularSpeed getShooterSpeed() {
		return shooterSpeed;
	}
	
	public boolean canSeeTarget() {
		return Time.getCurrentTime().sub(timeOfLastData).lessThan(FieldMap.MIN_TRACKING_WAIT_TIME);
	}
}
