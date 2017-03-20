import edu.nr.lib.NRMath;
import edu.nr.lib.Units;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Distance.Unit;

public class GearAlignCalculation {
	
	public static void main(String[] args) {
		updateDataType();
	}
	
	static Angle turnAngle = Angle.ZERO;
	static Distance driveDistance = Distance.ZERO;
			
	private static GearAlignCalculation singleton;
	
	public synchronized static void init() {
		if (singleton == null)
			singleton = new GearAlignCalculation();
	}
	
	public static GearAlignCalculation getInstance() {
		if (singleton == null) {
			init();
		}
		return singleton;
	}
	
	public static void updateDataType() {
		Angle lastSeenAngle = new Angle(30, Angle.Unit.DEGREE);
		Distance lastSeenDistance = new Distance(2, Distance.Unit.INCH);
		
		System.out.println("Dist: " + lastSeenDistance.get(Distance.Unit.INCH) + " angle: " + lastSeenAngle.get(Angle.Unit.DEGREE));

		Distance GEAR_TO_CENTER_DIST_X = new Distance(1, Distance.Unit.INCH);
		
		//System.out.println("gear to center dist x: " + RobotMap.GEAR_TO_CENTER_DIST_X.get(Unit.defaultUnit));
		System.out.println("lastSeenDistance: " + lastSeenDistance.get(Unit.defaultUnit));
		System.out.flush();
		Distance tempDistOne = NRMath.lawOfCos(lastSeenDistance, GEAR_TO_CENTER_DIST_X, Units.RIGHT_ANGLE.sub(lastSeenAngle));
		System.out.println("tempDistOne: " + tempDistOne.get(Distance.Unit.INCH));
		turnAngle = NRMath.lawOfCos(GEAR_TO_CENTER_DIST_X, tempDistOne, lastSeenDistance).sub(Units.RIGHT_ANGLE);
		
		System.out.println("turn angle: " + turnAngle.get(Angle.Unit.DEGREE));

		//driveDistance = NRMath.hypot(lastSeenDistance.mul(lastSeenAngle.cos()).add(GEAR_CAMERA_TO_CENTER_OF_ROBOT_DIST_Y), lastSeenDistance.mul(lastSeenAngle.sin())).sub(FieldMap.DRIVE_DEPTH_ON_PEG_FROM_SHIP);
		//turnAngle = NRMath.atan2(lastSeenDistance.mul(lastSeenAngle.sin()),lastSeenDistance.mul(lastSeenAngle.cos()).add(RobotMap.GEAR_CAMERA_TO_CENTER_OF_ROBOT_DIST_Y));
	}
}
