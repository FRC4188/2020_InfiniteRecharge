package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;

public class SpinIntake extends CommandBase {

    private final Intake intake;
    private final Magazine magazine;
    private final double percent;

    /**
     * Constructs new SpinIntake command to spin intake, indexer, and poly roller motors a given percentage.
     *
     * @param intake - Intake subsystem to use.
     * @param percent - percent output to command motors.
     */
    public SpinIntake(Intake intake, Magazine magazine, double percent) {
        this.percent = percent;
        this.magazine = magazine;
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (magazine.getTopBeam() && magazine.getBotBeam()) intake.spin(percent);
        else intake.spinIntake(percent);
    }

    @Override
    public void end(boolean interrupted) {
        intake.spin(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
