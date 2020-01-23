package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

/**
 * Runs shooter motors.
 */
public class RunShooter extends CommandBase {

    private final Shooter shooter;
    private double speed;

    /**
     * Constructs a new RunShooter command.
     *
     * @param shooter - Shooter subsystem to require,
     * @param xSpeed - Speed to spin motors at [-1.0, 1.0].
     */
    public RunShooter(Shooter shooter, double speed) {
        addRequirements(shooter);
        this.shooter = shooter;
        this.speed = speed;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.set(speed);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}