package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class FireIntake extends CommandBase {
    
    Intake intake;

    public FireIntake(Intake intake) {
        this.intake = intake;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.activateIntake(true);
    }

    @Override
    public void end(boolean interrupted) {
        intake.activateIntake(false);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}