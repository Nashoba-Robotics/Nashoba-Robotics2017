package edu.nr.robotics;

import edu.nr.lib.units.Angle.Type;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class RobotDiagram implements NamedSendable {

	private static RobotDiagram singleton;

	public static RobotDiagram getInstance() {
		if (singleton == null) {
			init();
		}
		return singleton;
	}

	public synchronized static void init() {
		if (singleton == null) {
			singleton = new RobotDiagram();
		}
	}

	private ITable table;

	private RobotDiagram() {
	}

	@Override
	public void initTable(ITable subtable) {
		this.table = subtable;
		if (table != null) {
			table.putNumber("Shooter Speed", Shooter.getInstance().getSpeed());
			table.putNumber("Hood Angle", Hood.getInstance().getPosition().get(Type.DEGREE));
			table.putNumber("Turret Angle", Turret.getInstance().getPosition().get(Type.DEGREE));
			table.putNumber("Match Time", DriverStation.getInstance().getMatchTime());
			table.putBoolean("Can Gear See", GearAlignCalculation.getInstance().canSeeTarget());

			table.putBoolean("Drive High Gear", Drive.getInstance().getCurrentGear() == Drive.Gear.high);

			table.putBoolean("Hood Good",
					StationaryTrackingCalculation.getInstance().getHoodAngle().sub(Hood.getInstance().getPosition())
							.lessThan(Hood.SHOOT_THRESHOLD)
							|| AutoTrackingCalculation.getInstance().getHoodAngle().sub(Hood.getInstance().getPosition()).abs()
							.lessThan(Hood.SHOOT_THRESHOLD));
			table.putBoolean("Turret Good",
					StationaryTrackingCalculation.getInstance().getTurretAngle().sub(Turret.getInstance().getPosition())
							.lessThan(Turret.SHOOT_THRESHOLD)
							|| AutoTrackingCalculation.getInstance().getTurretAngle().sub(Turret.getInstance().getPosition()).abs()
							.lessThan(Turret.SHOOT_THRESHOLD));
			table.putBoolean("Shooter Good",
					Math.abs(AutoTrackingCalculation.getInstance().getShooterSpeed()
							- Shooter.getInstance().getSpeed()) < Shooter.SHOOT_THRESHOLD
							|| Math.abs(StationaryTrackingCalculation.getInstance().getShooterSpeed()
									- Shooter.getInstance().getSpeed()) < Shooter.SHOOT_THRESHOLD);

			table.putBoolean("Hood Autotracking", Hood.getInstance().isAutoAlign());
			table.putBoolean("Turret Autotracking", Turret.getInstance().isAutoAlign());
			table.putBoolean("Shooter Autotracking", Shooter.getInstance().isAutoAlign());

		}

	}

	@Override
	public ITable getTable() {
		return table;
	}

	@Override
	public String getSmartDashboardType() {
		return "Robot Diagram";
	}

	@Override
	public String getName() {
		return "Robot Diagram";
	}

}
