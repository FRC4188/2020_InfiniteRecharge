package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class FireIntake extends CommandBase {
    
    Intake intake;
    boolean output;

    public FireIntake(Intake intake, boolean output) {
        this.intake = intake;
        this.output = output;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.activateIntake(output);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}