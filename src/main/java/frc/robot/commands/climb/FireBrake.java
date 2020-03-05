package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

/**
 * Fires solenoid to engage brake on climber.
 */
public class FireBrake extends CommandBase {

    private final Climber climber;

    /**
     * Constructs new FireBrake command to engage brake on climber.
     *
     * @param climber - Climber subsystem to use.
     */
    public FireBrake(Climber climber) {
        this.climber = climber;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        climber.engagePneuBrake(false);
    }

    @Override
    public void end(boolean interrupted) {
        climber.engagePneuBrake(true);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
