package frc.robot.commands.hood;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;

/**
 * Raises hood if it is currently lowered and vice versa.
 */
public class ToggleHood extends CommandBase {

    private final Hood hood;

    /**
     * Constructs new ToggleHood command to fire hood solenoids to
     *  raises hood if it is currently lowered and vice versa.
     *
     * @param hood - Hood subsystem to use.
     */
    public ToggleHood(Hood hood) {
        addRequirements(hood);
        this.hood = hood;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (hood.isRaised()) {
            hood.lower();
        } else {
            hood.raise();
        }
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
