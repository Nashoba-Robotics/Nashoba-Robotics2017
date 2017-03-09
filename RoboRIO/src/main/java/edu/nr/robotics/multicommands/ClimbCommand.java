package edu.nr.robotics.multicommands;

import edu.nr.robotics.subsystems.drive.DriveClimbCommand;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.nr.robotics.subsystems.turret.TurretPositionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

public class ClimbCommand extends CommandGroup {

	public ClimbCommand() {
		
	
		/*addSequential(new ConditionalCommand(new TurretPositionCommand(Turret.REVERSE_POSITION), new TurretPositionCommand(Turret.FORWARD_POSITION)) {

			@Override
			protected boolean condition() {
				return Turret.getInstance().getPosition().sub(Turret.REVERSE_POSITION).abs().lessThan(Turret.getInstance().getPosition().sub(Turret.FORWARD_POSITION).abs());
			}
			
		});*/
		addSequential(new DriveClimbCommand());
	}
	
}
