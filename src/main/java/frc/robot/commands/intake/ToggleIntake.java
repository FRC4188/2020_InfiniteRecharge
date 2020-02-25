package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

/**
 * Raises intake if it is currently lowered and vice versa.
 */
public class ToggleIntake extends CommandBase {

    private final Intake intake;

    /**
     * Constructs new Toggleintake command to fire intake solenoids.
     * Raises intake if it is currently lowered and vice versa.
     * @param intake - intake subsystem to use.
     */
    public ToggleIntake(Intake intake) {
        addRequirements(intake);
        this.intake = intake;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (intake.isRaised()) {
            intake.lower();
        } else {
            intake.raise();
        }
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}