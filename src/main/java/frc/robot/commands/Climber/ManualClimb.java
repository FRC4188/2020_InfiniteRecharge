package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ManualClimb extends CommandBase {
    Climber climber;
    double percent;
    /**
     * Creates a new RaiseClimber.
     */
    public ManualClimb(double percent, Climber climber) {
        addRequirements(climber);
        this.percent = percent;
        this.climber = climber;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if((climber.getLeftPosition() < 100000) && (climber.getLeftPosition() >= 0)) {
            climber.setSpeedPercentage(percent);
        } else {
            climber.setSpeedPercentage(0);
        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.setSpeedPercentage(0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}