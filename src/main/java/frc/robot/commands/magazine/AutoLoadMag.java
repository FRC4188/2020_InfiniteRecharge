package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;

/**
 * Runs magazine motor at a given percentage.
 */
public class AutoLoadMag extends CommandBase {

    private final Magazine magazine;
    private final double kP = 0.00005;
    private final double kD = 0.0005;
    private final double DELTA_T = 0.02;
    private final double TOLERANCE = 1;
    private double position;
    private double targetPosition;
    private double count;
    private double lastError;

    /**
     * Constructs a new RunMagazine command to run magazine motor at a given percentage.
     * @param magazine - Magazine subsystem to use.
     * @param percent - Percent to run motors [-1.0, 1.0], positive feeds shooter.
     */
    public AutoLoadMag(Magazine magazine, double targetPosition) {
        addRequirements(magazine);
        this.magazine = magazine;
        this.targetPosition = targetPosition;
    }

    @Override
    public void initialize() {
        targetPosition += magazine.getPosition();
        lastError = 0;
    }

    @Override
    public void execute() {
        position = magazine.getPosition();
        double error = targetPosition - position;
        double output = kP * (error) + kD * DELTA_T * lastError;
        magazine.set(output);
        lastError = error;
        if (Math.abs(position - targetPosition) < TOLERANCE) count++;
        else count = 0;
    }

    @Override
    public boolean isFinished() {
        return count > 10;
    }

    @Override
    public void end(boolean interrupted) {
        magazine.set(0);
    }

}
