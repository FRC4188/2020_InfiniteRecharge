package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;

/**
 * Runs magazine motor at a given percentage.
 */
public class LoadOne extends CommandBase {

    private final Magazine magazine;
    private final Intake intake;
    private final double DELTA_T = 0.02;
    private final double TIMEOUT = 5 / DELTA_T;
    private double timer;

    /**
     * Constructs a new RunMagazine command to run magazine motor at a given percentage.
     * @param magazine - Magazine subsystem to use.
     * @param intake - Intake subsystem to use.
     */
    public LoadOne(Magazine magazine, Intake intake) {
        addRequirements(magazine, intake);
        this.magazine = magazine;
        this.intake = intake;
    }

    @Override
    public void initialize() {
        timer = 0;
    }

    @Override
    public void execute() {
        if (magazine.getCount() < 3) {
            if (magazine.getBotBeam()) {
                magazine.set(0.8);
                intake.spinIndexer(0.3);
                intake.spinPolyRollers(0.3);
                timer = 0;
            }
            else {
                intake.spinIndexer(0.6);
                intake.spinPolyRollers(0.6);
                timer++;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return (!magazine.getBotBeam()) || timer > TIMEOUT || magazine.getCount() >= 3;
    }

    @Override
    public void end(boolean interrupted) {
        magazine.setCount();
        magazine.set(0);
        intake.spinIndexer(0);
        intake.spinPolyRollers(0);
    }

}
