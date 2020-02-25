package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class SpinIndexer extends CommandBase {

    Intake intake;
    double percent;
    boolean override;

    /**
     * Constructs new SpinIndexer command to spin indexer motors a given percentage.
     *
     * @param intake - Intake subsystem to use.
     * @param percent - percent out to command motors.
     */
    public SpinIndexer(Intake intake, double percent) {
        this.percent = percent;
        this.intake = intake;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.spinIndexer(percent);
        intake.spinPolyRollers(percent);
    }

    @Override
    public void end(boolean interrupted) {
        intake.spinIndexer(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
