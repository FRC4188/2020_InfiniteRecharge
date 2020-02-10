package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;

public class SpinShooter extends CommandBase{

    private Shooter shooter;
    private Limelight limelight;

    public SpinShooter(Shooter shooter, Limelight limelight) {
        addRequirements(shooter, limelight);
        this.shooter = shooter;
        this.limelight = limelight;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setVelocity(limelight.formulaRPM());
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setCoast();
    }
}