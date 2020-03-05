package frc.robot.commands.hood;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;

public class RaiseHood extends CommandBase {

    private final Hood hood;

    /**
     * Constructs new RaiseHood command to fire hood solenoids to raised position.
     *
     * @param hood - Hood subsystem to use.
     */
    public RaiseHood(Hood hood) {
        addRequirements(hood);
        this.hood = hood;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        hood.raise();
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
