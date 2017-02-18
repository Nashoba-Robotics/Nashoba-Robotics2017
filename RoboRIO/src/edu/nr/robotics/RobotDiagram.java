package edu.nr.robotics;

import edu.nr.lib.Units;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.NamedSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class RobotDiagram implements NamedSendable {

	private ITable table;
	
	public RobotDiagram() {
	}

	@Override
	public void initTable(ITable subtable) {
		this.table = subtable;
		if (table != null) {
			table.putNumber("Shooter Speed", Shooter.getInstance().getSpeed());
			table.putNumber("Hood Angle", Hood.getInstance().getPosition() * Units.DEGREES_PER_ROTATION);
			table.putNumber("Turret Angle", Turret.getInstance().getPosition() * Units.DEGREES_PER_ROTATION);
			table.putNumber("Match Time", DriverStation.getInstance().getMatchTime());
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
