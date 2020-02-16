package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class SpinIntake extends CommandBase {

    private final Intake intake;
    double percent;
    boolean humanPlayer;

    public SpinIntake(Intake intake, double percent) {
        this.intake = intake;
        this.percent = percent;
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
        //if(!humanPlayer) {
        intake.spinIndexer(0.5*percent);
        intake.spinIntake(percent);
        intake.spinPolyRollers(0.3*percent);
        //}
        //else {
        //    intake.spinIntake(-percent);
        //    intake.spinIndexer(0.5*percent);
        //    intake.spinPolyRollers(0.3*percent);
        //}
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}