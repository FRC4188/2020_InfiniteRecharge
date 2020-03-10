package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;

/**
 * Runs magazine and indexer motors to feed cells if shooter is at correct rpm.
 */
public class MagBallCount extends CommandBase {

    private final Magazine magazine;
    private int change;

    /**
     * Constructs a new AutoMagazine command to feed cells if shooter is at correct rpm.
     *
     * @param magazine - Magazine subsystem to use for turning motors.
     * @param change - The amount to change the ball count by.
     */
    public MagBallCount(Magazine magazine, int change) {
        addRequirements(magazine);
        this.magazine = magazine;
        this.change = change;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        magazine.setCount(change);

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }

}
