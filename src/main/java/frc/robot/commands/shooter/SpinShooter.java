package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

/**
 * Spins shooter to a given velocity.
 */
public class SpinShooter extends CommandBase {

    private final Shooter shooter;
    private final double rpm;

    /**
     * Constructs a new SpinShooter command to spin shooter to given velocity.
     *
     * @param shooter - Shooter subsystem to use.
     * @param rpm - Velocity to spin shooter to in rpm.
     */
    public SpinShooter(Shooter shooter, double rpm) {
        addRequirements(shooter);
        this.shooter = shooter;
        this.rpm = rpm;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setVelocity(rpm);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }

}
