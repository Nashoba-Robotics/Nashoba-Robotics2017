package edu.nr.robotics.subsystems.intake;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class IntakeStopCommand extends CommandGroup {

    public IntakeStopCommand() {
        addSequential(new IntakeSpeedCommand(0));
    }
}
