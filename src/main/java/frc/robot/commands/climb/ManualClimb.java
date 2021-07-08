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

        climber.engagePneuBrake(true);

        // Will independently command motors if they are uneven in height.
        if ((climber.getLeftPosition() >= climber.getMaxPosition() && percent > 0)
                || (climber.getLeftPosition() <= climber.getMinPosition() && percent < 0)) {
            climber.setLeft(0);
        } else if ((climber.getLeftPosition() >= climber.getMaxPosition() - 20000 && percent > 0)
                || (climber.getLeftPosition() <= climber.getMinPosition() + 20000 && percent < 0)) {
            climber.setLeft(percent / 3.0);
        } else {
            climber.setLeft(percent);
        }

        if ((climber.getRightPosition() >= climber.getMaxPosition() && percent > 0)
                || (climber.getRightPosition() <= climber.getMinPosition() && percent < 0)) {
            climber.setRight(0);
        } else if ((climber.getRightPosition() >= climber.getMaxPosition() - 20000 && percent > 0)
                || (climber.getRightPosition() <= climber.getMinPosition() + 20000 && percent < 0)) {
            climber.setRight(percent / 3.0);
        } else {
            climber.setRight(percent);
        }

    }

    @Override
    public void end(boolean interrupted) {
        climber.engagePneuBrake(true);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
