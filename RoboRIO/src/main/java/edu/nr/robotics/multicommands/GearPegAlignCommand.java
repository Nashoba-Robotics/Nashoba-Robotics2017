package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.GearAlignCalculation;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingExtendableCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleExtendableCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class GearPegAlignCommand extends CommandGroup {

    public GearPegAlignCommand() {
    	addSequential(new NRCommand() {
    		@Override
    		public void onExecute() {
    			System.out.println("Waiting for seeing gear");
    		}
    		
    		@Override
			protected boolean isFinishedNR() {
    			return GearAlignCalculation.getInstance().canSeeTarget();
    		}
    	});
    	
    	addSequential(new NRCommand() {
    		@Override
    		public void onStart() {
    			System.out.println("About to turn");
    		}
    	});
    	
    	addSequential(new DrivePIDTurnAngleExtendableCommand() {

			@Override
			public Angle getAngleToTurn() {
				return GearAlignCalculation.getInstance().getAngleToTurn();
			}
    		
    	});
    	
    	addSequential(new NRCommand() {
    		@Override
    		public void onStart() {
    			System.out.println("Finished turning");
    		}
    	});
    	
    	addSequential(new DriveForwardProfilingExtendableCommand() {

			@Override
			public Distance distanceToGo() {
				return GearAlignCalculation.getInstance().getDistToDrive().negate();
			}
    		
    	});
    	
    	addSequential(new NRCommand() {
    		@Override
    		public void onStart() {
    			System.out.println("Finished driving");
    		}
    	});
    }
}
