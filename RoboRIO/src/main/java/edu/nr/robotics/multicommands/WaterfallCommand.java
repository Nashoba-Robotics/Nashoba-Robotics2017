package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.CancelAllCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.subsystems.agitator.Agitator;
import edu.nr.robotics.subsystems.agitator.AgitatorRunCommand;
import edu.nr.robotics.subsystems.gearMover.GearDeployCommand;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodPositionCommand;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.shooter.ShooterSpeedCommand;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.nr.robotics.subsystems.turret.TurretPositionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class WaterfallCommand extends CommandGroup {

	public WaterfallCommand() {
		addSequential(new GearDeployCommand());
		addSequential(new HoodPositionCommand(Hood.TOP_POSITION));
		addSequential(new TurretPositionCommand(new Angle(30, Angle.Unit.DEGREE)));
		addSequential(new ShooterSpeedCommand(new AngularSpeed(500, Angle.Unit.ROTATION, Time.Unit.MINUTE)));
		addSequential(new AgitatorRunCommand());
		addSequential(new WaitCommand(10));
		addSequential(new CancelAllCommand());
	}
}
