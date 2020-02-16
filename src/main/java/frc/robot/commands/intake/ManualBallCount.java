package frc.robot.commands.intake;

import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManualBallCount extends CommandBase {

    private final Intake intake;
    private int value;

    /**
     * Changes the value of the ball count.
     */
    public ManualBallCount(int value, Intake intake) {
        this.intake = intake;
        this.value = value;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
      intake.changeBallCount(value);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

  
}