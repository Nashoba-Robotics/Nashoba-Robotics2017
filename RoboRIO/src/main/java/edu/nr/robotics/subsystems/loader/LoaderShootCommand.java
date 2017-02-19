package edu.nr.robotics.subsystems.loader;

import edu.nr.robotics.AutoTrackingCalculation;
import edu.nr.robotics.StationaryTrackingCalculation;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

public class LoaderShootCommand extends CommandGroup {

	public LoaderShootCommand() {
		addSequential(new ConditionalCommand(new LoaderRunCommand()) {

			@Override
			protected boolean condition() {
				if (AutoTrackingCalculation.getInstance().getHoodAngle().sub(Hood.getInstance().getPosition()).abs().lessThan(Hood.SHOOT_THRESHOLD)
						&& Math.abs(AutoTrackingCalculation.getInstance().getShooterSpeed() - Shooter.getInstance().getSpeed()) < Shooter.SHOOT_THRESHOLD
						&& AutoTrackingCalculation.getInstance().getTurretAngle().sub(Turret.getInstance().getPosition()).abs().lessThan(Turret.SHOOT_THRESHOLD)) {
					return true;
				}
				if (StationaryTrackingCalculation.getInstance().getHoodAngle().sub(Hood.getInstance().getPosition()).abs().lessThan(Hood.SHOOT_THRESHOLD)
						&& Math.abs(StationaryTrackingCalculation.getInstance().getShooterSpeed() - Shooter.getInstance().getSpeed()) < Shooter.SHOOT_THRESHOLD
						&& StationaryTrackingCalculation.getInstance().getTurretAngle().sub(Turret.getInstance().getPosition()).abs().lessThan(Turret.SHOOT_THRESHOLD)) {
					return true;
				}
				return false;			
			}
			
		});
	}
}
