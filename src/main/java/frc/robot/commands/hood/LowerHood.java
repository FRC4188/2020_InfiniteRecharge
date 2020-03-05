package frc.robot.commands.hood;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;

public class LowerHood extends CommandBase {

    private final Hood hood;

    /**
     * Constructs new LowerHood command to fire hood solenoids to lowered position.
     *
     * @param hood - Hood subsystem to use.
     */
    public LowerHood(Hood hood) {
        addRequirements(hood);
        this.hood = hood;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        hood.lower();
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
