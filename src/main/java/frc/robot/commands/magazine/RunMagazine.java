package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;

/**
 * Runs magazine motor at a given percentage.
 */
public class RunMagazine extends CommandBase {

    private final Magazine magazine;
    private final double percent;
    private boolean manual;

    /**
     * Constructs a new RunMagazine command to run magazine motor at a given percentage.
     * @param magazine - Magazine subsystem to use.
     * @param percent - Percent to run motors [-1.0, 1.0], positive feeds shooter.
     */
    public RunMagazine(Magazine magazine, double percent, boolean manual) {
        addRequirements(magazine);
        this.magazine = magazine;
        this.percent = percent;
        this.manual = manual;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        magazine.set(percent);
        magazine.setManual(manual);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }

}
