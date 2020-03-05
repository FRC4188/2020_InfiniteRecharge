package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;

/**
 * Spins shooter to a velocity calculated by Limelight.
 */
public class SpinShooterFormula extends CommandBase {

    private final Shooter shooter;
    private final Limelight limelight;

    /**
     * Constructs a new SpinShooterFormula command to spin shooter
     * to a velocity calculated by Limelight.
     *
     * @param shooter - Shooter subsystem to use.
     * @param limelight - Limelight subsystem to use.
     */
    public SpinShooterFormula(Shooter shooter, Limelight limelight) {
        addRequirements(shooter);
        this.shooter = shooter;
        this.limelight = limelight;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setVelocity(limelight.formulaRpm());
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        shooter.set(0);
    }

}
