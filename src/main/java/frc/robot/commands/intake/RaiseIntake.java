package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class RaiseIntake extends CommandBase {

    private final Intake intake;

    /**
     * Constructs new RaiseIntake command to fire intake solenoids to raised position.
     *
     * @param intake - Intake subsystem to use.
     */
    public RaiseIntake(Intake intake) {
        this.intake = intake;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.raise();
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
