package edu.nr.robotics.auton;

import edu.nr.lib.commandbased.AnonymousCommandGroup;
import edu.nr.lib.network.TCPServer;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.FieldMap;
import edu.nr.robotics.GearAlignCalculation;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.GearPegAlignCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.loader.LoaderRunCommand;
import edu.nr.robotics.subsystems.loader.LoaderStopCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class DriveToMiddleGearAutoCommand extends CommandGroup {
	
	public DriveToMiddleGearAutoCommand() {
		addSequential(new RequiredAutoCommand());
		
		addSequential(new DriveForwardProfilingCommand((FieldMap.DISTANCE_TO_CENTER_PEG.sub(RobotMap.BACK_BUMPER_TO_GEAR_DIST).sub(FieldMap.DRIVE_DEPTH_ON_PEG_FROM_SHIP).sub(FieldMap.GEAR_ALIGN_STOP_DISTANCE_FROM_PEG)).negate(),0.5)); //Negated for driving backwards in auto
		
		
		addParallel(new AnonymousCommandGroup() {

			@Override
			public void commands() {
				addSequential(new WaitCommand(4));

				addParallel(new EnableAutoTrackingCommandAuton());
				
				addSequential(new WaitCommand(2));
				
				addSequential(new LoaderRunCommand());
				
			}
			
		});

		addSequential(new WaitCommand(1));

		addSequential(new GearPegAlignCommand());
		
				

		
		/*addSequential(new ConditionalCommand(new GearPegAlignCommand(),new DriveForwardProfilingCommand(new Distance(-36, Distance.Unit.INCH),0.5)) {

			@Override
			protected boolean condition() {
				return TCPServer.Num.gear.getInstance().isConnected() && GearAlignCalculation.getInstance().canSeeTarget();
			}
			
		});*/

	}
}
