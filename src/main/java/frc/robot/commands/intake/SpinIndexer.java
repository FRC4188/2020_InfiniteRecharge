package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class SpinIndexer extends CommandBase {
    
    Intake intake;
    double percent;
    boolean override;

    public SpinIndexer(Intake intake, double percent) {
        this.percent = percent;
        this.intake = intake;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        /*if(intake.indexerInterval()) {
            intake.spinIndexer(0.3*percent);
            intake.spinIntake(percent);
        }
        else {
            intake.spinIntake(percent);
        }*/
        //intake.spinIndexer(percent);
        intake.spinIndexer(percent);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}