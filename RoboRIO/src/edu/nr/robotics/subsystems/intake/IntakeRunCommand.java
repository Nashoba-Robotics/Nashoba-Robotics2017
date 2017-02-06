package edu.nr.robotics.subsystems.intake;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class IntakeRunCommand extends CommandGroup {

    public IntakeRunCommand() {
        addSequential(new IntakeSpeedCommand(RobotMap.INTAKE_RUN_SPEED));
    }
}
