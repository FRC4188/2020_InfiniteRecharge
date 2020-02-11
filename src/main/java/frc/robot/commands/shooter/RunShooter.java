package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

/**
 * Runs shooter motors.
 */
public class RunShooter extends CommandBase {

    private final Shooter shooter;
    private double percent;

    /**
     * Constructs a new RunShooter command.
     *
     * @param shooter - Shooter subsystem to require,
     * @param percent - Percent to spin motors at [-1.0, 1.0].
     */
    public RunShooter(Shooter shooter, double percent) {
        addRequirements(shooter);
        this.shooter = shooter;
        this.percent = percent;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.set(percent);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}