package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;

/**
 * Runs magazine motor at a given percentage.
 */
public class RunMagazine extends CommandBase {

    private final Magazine magazine;
    private final double percent;
    private boolean can;
    private double count;

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
        can = false;
    }

    @Override
    public void execute() {
        /*count = magazine.getCount();
        if (!magazine.getTopBeam()) {
            can = true;
        }
        else {
            if (can) magazine.setCount(count - 1);
            can = false;
        }*/
        magazine.set(percent);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }

}
