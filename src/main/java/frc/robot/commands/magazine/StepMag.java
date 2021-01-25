package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;

/**
 * Runs magazine motor at a given percentage.
 */
public class StepMag extends CommandBase {

    private final Magazine magazine;
    private double inches;
    private double goal;

    private double I_TOLERANCE = 0.1;
    private double V_TOLERANCE = 0.2;

    /**
     * Constructs a new RunMagazine command to run magazine motor at a given percentage.
     * @param magazine - Magazine subsystem to use.
     * @param percent - Percent to run motors [-1.0, 1.0], positive feeds shooter.
     */
    public StepMag(Magazine magazine, double inches) {
        addRequirements(magazine);
        this.magazine = magazine;
        this.inches = inches;
    }

    @Override
    public void initialize() {
        goal = magazine.getPosition() + inches;
    }

    @Override
    public void execute() {
        magazine.setPosition(goal);
    }

    @Override
    public boolean isFinished() {
        return (magazine.getPosition() - goal) <= I_TOLERANCE && magazine.getVelocity() <= V_TOLERANCE;
    }

    @Override
    public void end(boolean interrupted) {
        magazine.set(0);
    }

}
