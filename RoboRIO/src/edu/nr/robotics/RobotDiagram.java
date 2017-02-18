package edu.nr.robotics;

import edu.nr.lib.Units;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodStationaryAngleCorrectionCommand;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.shooter.ShooterStationarySpeedCorrectionCommand;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.nr.robotics.subsystems.turret.TurretStationaryAngleCorrectionCommand;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class RobotDiagram implements NamedSendable {

	private static RobotDiagram singleton;
	
	public static RobotDiagram getInstance() {
		if(singleton == null) {
			init();
		}
		return singleton;
	}
	
	public synchronized static void init() {
		if(singleton == null) {
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
			table.putNumber("Hood Angle", Hood.getInstance().getPosition() * Units.DEGREES_PER_ROTATION);
			table.putNumber("Turret Angle", Turret.getInstance().getPosition() * Units.DEGREES_PER_ROTATION);
			table.putNumber("Match Time", DriverStation.getInstance().getMatchTime());
			table.putBoolean("Can Gear See", GearAlignCalculation.getInstance().canSeeTarget());

			table.putBoolean("Drive High Gear", Drive.getInstance().getCurrentGear() == Drive.Gear.high);

			table.putBoolean("Hood Good", Math.abs(HoodStationaryAngleCorrectionCommand.getHoodAngle() - Hood.getInstance().getPosition()) < Hood.SHOOT_THRESHOLD / Units.DEGREES_PER_ROTATION || Math.abs(AutoTrackingCalculation.getInstance().getHoodAngle() - Hood.getInstance().getPosition()) < Hood.SHOOT_THRESHOLD / Units.DEGREES_PER_ROTATION);
			table.putBoolean("Turret Good", Math.abs(TurretStationaryAngleCorrectionCommand.getTurretAngle() - Turret.getInstance().getPosition()) < Turret.SHOOT_THRESHOLD / Units.DEGREES_PER_ROTATION || Math.abs(AutoTrackingCalculation.getInstance().getTurretAngle() - Turret.getInstance().getPosition()) < Turret.SHOOT_THRESHOLD / Units.DEGREES_PER_ROTATION);
			table.putBoolean("Shooter Good", Math.abs(AutoTrackingCalculation.getInstance().getShooterSpeed() - Shooter.getInstance().getSpeed()) < Shooter.SHOOT_THRESHOLD || Math.abs(ShooterStationarySpeedCorrectionCommand.getShooterSpeed() - Shooter.getInstance().getSpeed()) < Shooter.SHOOT_THRESHOLD);
		
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
