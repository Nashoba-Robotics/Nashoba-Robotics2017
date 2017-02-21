package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.GearAlignCalculation;
import edu.nr.robotics.subsystems.drive.DriveForwardCommand;
import edu.nr.robotics.subsystems.drive.DrivePIDTurnAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class GearPegAlignCommand extends CommandGroup {

    public GearPegAlignCommand(boolean negate) {
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
    		DriveForwardCommand driveCommand;
    		
    		@Override
    		public void onStart() {
    			if (!negate) {
    				driveCommand = new DriveForwardCommand(GearAlignCalculation.getInstance().getDistToDrive());
    				driveCommand.start();
    			} else {
    				driveCommand = new DriveForwardCommand(GearAlignCalculation.getInstance().getDistToDrive().negate());
    				driveCommand.start();
    			}
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
