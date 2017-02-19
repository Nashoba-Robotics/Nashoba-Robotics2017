package edu.nr.robotics.auton;

import edu.nr.robotics.Robot;
import edu.nr.robotics.subsystems.gearMover.GearDeployCommand;
import edu.nr.robotics.subsystems.hood.HoodPositionCommand;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.nr.robotics.subsystems.turret.TurretPositionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public final class RequiredAutoCommand extends CommandGroup {

	public RequiredAutoCommand() {
		addParallel(new GearDeployCommand());
		addParallel(new HoodPositionCommand(0));
		if (Robot.side == SideOfField.blue) {
			addParallel(new TurretPositionCommand(Turret.REVERSE_POSITION));
		} else {
			addParallel(new TurretPositionCommand(Turret.FORWARD_POSITION));
		}
	}
}
