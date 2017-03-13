package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.GearAlignCalculation;
import edu.nr.robotics.subsystems.drive.DriveForwardProfilingCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class GearPegAlignCommand extends CommandGroup {

    public GearPegAlignCommand() {
    	addSequential(new NRCommand() {
    		@Override
			protected boolean isFinishedNR() {
    			return GearAlignCalculation.getInstance().canSeeTarget();
    		}
    	});
    	
    	addSequential(new NRCommand() {
    		DrivePIDTurnAngleCommand turnCommand;
    		
    		@Override
    		public void onStart() {
    			turnCommand = new DrivePIDTurnAngleCommand(GearAlignCalculation.getInstance().getAngleToTurn());
    			turnCommand.start();
    		}
    		
    		@Override
    		public boolean isFinishedNR() {
    			if(turnCommand == null) {
    				return false;
    			}
    			return turnCommand.isFinishedNR();
    		}
    		
    		@Override
    		public void onEnd() {
    			turnCommand.cancel();
    		}
    	});
    	

    	
    	addSequential(new NRCommand() {
    		DriveForwardProfilingCommand driveCommand;
    		
    		@Override
    		public void onStart() {
    			driveCommand = new DriveForwardProfilingCommand(GearAlignCalculation.getInstance().getDistToDrive().negate());
    			driveCommand.start();
    		}
    		
    		@Override
    		public boolean isFinishedNR() {
    			if(driveCommand == null) {
    				return false;
    			}
    			return driveCommand.isFinishedNR();
    		}
    		
    		@Override
    		public void onEnd() {
    			driveCommand.cancel();
    		}
    	});
    }
}
