package edu.nr.robotics;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Angle.Unit;
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
			table.putNumber("Shooter Speed", Shooter.getInstance().getSpeed().get(Angle.Unit.ROTATION, Time.Unit.MINUTE));
			table.putNumber("Hood Angle", Hood.getInstance().getPosition().get(Unit.DEGREE));
			table.putNumber("Turret Angle", Turret.getInstance().getPosition().get(Unit.DEGREE));
			table.putNumber("Match Time", DriverStation.getInstance().getMatchTime());
			table.putBoolean("Is Auto", Robot.getInstance().isAutonomous());
			table.putBoolean("Can Gear See", GearAlignCalculation.getInstance().canSeeTarget());

			table.putBoolean("Drive High Gear", Drive.getInstance().getCurrentGear() == Drive.Gear.high);

			table.putBoolean("Hood Aligned",
					StationaryTrackingCalculation.getInstance().getHoodAngle().sub(Hood.getInstance().getPosition())
							.lessThan(Hood.SHOOT_THRESHOLD)
							|| AutoTrackingCalculation.getInstance().getHoodAngle().sub(Hood.getInstance().getPosition()).abs()
							.lessThan(Hood.SHOOT_THRESHOLD));
			table.putBoolean("Turret Aligned",
					StationaryTrackingCalculation.getInstance().getTurretAngle().sub(Turret.getInstance().getPosition())
							.lessThan(Turret.SHOOT_THRESHOLD)
							|| AutoTrackingCalculation.getInstance().getTurretAngle().sub(Turret.getInstance().getPosition()).abs()
							.lessThan(Turret.SHOOT_THRESHOLD));
			table.putBoolean("Shooter Aligned",
					AutoTrackingCalculation.getInstance().getShooterSpeed().sub(Shooter.getInstance().getSpeed()).abs().lessThan(Shooter.SHOOT_THRESHOLD) ||
					StationaryTrackingCalculation.getInstance().getShooterSpeed().sub(Shooter.getInstance().getSpeed()).abs().lessThan(Shooter.SHOOT_THRESHOLD));

			table.putBoolean("Hood Tracking", Hood.getInstance().isAutoAlign());
			table.putBoolean("Turret Tracking", Turret.getInstance().isAutoAlign());
			table.putBoolean("Shooter Tracking", Shooter.getInstance().isAutoAlign());

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
