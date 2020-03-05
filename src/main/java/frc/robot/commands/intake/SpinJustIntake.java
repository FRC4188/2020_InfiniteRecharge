package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class SpinJustIntake extends CommandBase {

    private final Intake intake;
    private final double percent;

    /**
     * Constructs new SpinJustIntake command to spin just the intake motors a given percentage.
     *
     * @param intake - Intake subsystem to use.
     * @param percent - percent output to command motors.
     */
    public SpinJustIntake(Intake intake, double percent) {
        this.percent = percent;
        this.intake = intake;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.spinIntake(percent);
    }

    @Override
    public void end(boolean interrupted) {
        intake.spinIntake(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
