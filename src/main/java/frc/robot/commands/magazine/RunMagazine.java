package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;

/**
 * Runs magazine motor at a given percentage.
 */
public class RunMagazine extends CommandBase {

    private final Magazine magazine;
    private final double percent;

    /**
     * Constructs a new RunMagazine command to run magazine motor at a given percentage.
     * @param magazine - Magazine subsystem to use.
     * @param percent - Percent to run motors [-1.0, 1.0], positive feeds shooter.
     */
    public RunMagazine(Magazine magazine, double percent) {
        addRequirements(magazine);
        this.magazine = magazine;
        this.percent = percent;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        magazine.set(percent);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        magazine.set(0);
    }

}
