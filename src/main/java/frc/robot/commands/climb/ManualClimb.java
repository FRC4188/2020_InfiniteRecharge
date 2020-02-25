package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

/**
 * Controls climber manually.
 */
public class ManualClimb extends CommandBase {

    private final Climber climber;
    private final double percent;

    /**
     * Constructs a new ManualClimb command to run climber motors at a given percentage.
     *
     * @param climber - Climber subsystem to use.
     * @param percent - percent to drive motors, positive extends.
     */
    public ManualClimb(Climber climber, double percent) {
        addRequirements(climber);
        this.climber = climber;
        this.percent = percent;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if ((climber.getLeftPosition() >= climber.getMaxPosition() && percent > 0)
                || (climber.getLeftPosition() <= climber.getMinPosition() && percent < 0)) {
            climber.setSpeedPercentage(0);
        } else {
            climber.setSpeedPercentage(percent);
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
