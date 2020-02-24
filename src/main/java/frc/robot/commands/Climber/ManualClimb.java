package frc.robot.commands.climber;

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
            climber.setLeftPercentage(0);
        } else if ((climber.getLeftPosition() >= climber.getMaxPosition() - 20000 && percent > 0)
                || (climber.getLeftPosition() <= climber.getMinPosition() + 20000 && percent < 0)){
            climber.setLeftPercentage(percent / 3);
        } else {
            climber.setLeftPercentage(percent);
        }

        if ((climber.getRightPosition() >= climber.getMaxPosition() && percent > 0)
                || (climber.getRightPosition() <= climber.getMinPosition() && percent < 0)) {
            climber.setRightPercentage(0);
        } else if ((climber.getRightPosition() >= climber.getMaxPosition() - 20000 && percent > 0)
                || (climber.getRightPosition() <= climber.getMinPosition() + 20000 && percent < 0)){
            climber.setRightPercentage(percent / 3);
        } else {
            climber.setRightPercentage(percent);
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
