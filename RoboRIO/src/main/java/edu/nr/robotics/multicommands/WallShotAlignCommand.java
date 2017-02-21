package edu.nr.robotics.multicommands;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.subsystems.hood.HoodPositionCommand;
import edu.nr.robotics.subsystems.shooter.ShooterSpeedCommand;
import edu.nr.robotics.subsystems.turret.TurretPositionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class WallShotAlignCommand extends CommandGroup {

	// TODO: Wall shot: Get preset hood position and shooter speed
	private static final Angle WALL_SHOT_HOOD_POSITION = new Angle(0, Angle.Unit.DEGREE);
	private static final AngularSpeed WALL_SHOT_SHOOTER_SPEED = new AngularSpeed(0, Angle.Unit.ROTATION, Time.Unit.MINUTE);
	
	public WallShotAlignCommand() {
		addSequential(new HoodPositionCommand(WALL_SHOT_HOOD_POSITION));
		addSequential(new ShooterSpeedCommand(WALL_SHOT_SHOOTER_SPEED));
		addSequential(new TurretPositionCommand(new Angle(0.0, Angle.Unit.DEGREE)));
	}
}
