package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.subsystems.intake.Intake;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeSetPIDSmartDashboardCommand extends NRCommand {

	public IntakeSetPIDSmartDashboardCommand(){
		requires(Intake.getInstance());
	}
	
	@Override
	protected void onStart() {
		SmartDashboard.putNumber("Intake P Low", SmartDashboard.getNumber("Intake P Low", Intake.P_LOW));
		SmartDashboard.putNumber("Intake I Low", SmartDashboard.getNumber("Intake I Low", Intake.I_LOW));
		SmartDashboard.putNumber("Intake D Low", SmartDashboard.getNumber("Intake D Low", Intake.D_LOW));
		SmartDashboard.putNumber("Intake F Low", SmartDashboard.getNumber("Intake F Low", Intake.F_LOW));
		
		SmartDashboard.putNumber("Intake P High", SmartDashboard.getNumber("Intake P High", Intake.P_HIGH));
		SmartDashboard.putNumber("Intake I High", SmartDashboard.getNumber("Intake I High", Intake.I_HIGH));
		SmartDashboard.putNumber("Intake D High", SmartDashboard.getNumber("Intake D High", Intake.D_HIGH));
		SmartDashboard.putNumber("Intake F High", SmartDashboard.getNumber("Intake F High", Intake.F_HIGH));
		
		Intake.getInstance().setPID(SmartDashboard.getNumber("Intake P Low", Intake.P_LOW), 
								     SmartDashboard.getNumber("Intake I Low", Intake.I_LOW), 
								     SmartDashboard.getNumber("Intake D Low", Intake.D_LOW), 
								     SmartDashboard.getNumber("Intake F Low", Intake.F_LOW),
									 SmartDashboard.getNumber("Intake P High", Intake.P_HIGH),
									 SmartDashboard.getNumber("Intake I High", Intake.I_HIGH),
									 SmartDashboard.getNumber("Intake D High", Intake.D_HIGH),
									 SmartDashboard.getNumber("Intake F High", Intake.F_HIGH));
	}
}
