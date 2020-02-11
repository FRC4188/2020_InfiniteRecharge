package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

/**
 * Runs shooter motors at a given velocity.
 */
public class RunShooterVelocity extends CommandBase {

    private final Shooter shooter;
    private double velocity;

    /**
     * Constructs a new RunShooterVelocity command.
     *
     * @param shooter - Shooter subsystem to require,
     * @param velocity - Velocity to spin motors to in rpm.
     */
    public RunShooterVelocity(Shooter shooter, double velocity) {
        addRequirements(shooter);
        this.shooter = shooter;
        this.velocity = velocity;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setVelocity(velocity);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}