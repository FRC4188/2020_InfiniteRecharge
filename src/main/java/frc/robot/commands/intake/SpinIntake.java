package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;

public class SpinIntake extends CommandBase {

    private final Intake intake;
    private final Magazine magazine;
    private final double percent;
    private final double indexer;

    /**
     * Constructs new SpinIntake command to spin intake, indexer, and poly roller motors a given percentage.
     *
     * @param intake - Intake subsystem to use.
     * @param percent - percent output to command motors.
     */
    public SpinIntake(Intake intake, Magazine magazine, double percent, double indexer) {
        this.percent = percent;
        this.magazine = magazine;
        this.intake = intake;
        this.indexer = indexer;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (!magazine.topBeamClear()) intake.spin(percent, 0.0);
        else {
            if (!magazine.midBeamClear()) {
                intake.spin(percent, -0.2);
            }
            else intake.spin(percent, indexer);
        }

        // With Bot Beam
        /*if (!magazine.topBeamClear()) intake.spin(percent, 0.0, 0.0);
        else {
            if (!magazine.botBeamClear()) {
                intake.spin(percent, 0.0, 0.0);
            }
            else intake.spin(percent, indexer, poly);
        }*/

        // Original
        /*if (magazine.getTopBeam()) intake.spin(percent, indexer, poly);
        else if (!magazine.getBotBeam()) {
            intake.spin(percent, -0.1, 0.0);
        }
        else intake.spin(percent, 0.0, 0.0);*/
    }

    @Override
    public void end(boolean interrupted) {
        intake.spin(0.0, 0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}