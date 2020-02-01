package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class SpinShooter extends CommandBase{

    private Shooter shooter;
    private double velocity; //RPM

    public SpinShooter(Shooter shooter, double velocity) {
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
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) shooter.setVelocity(0);
        else shooter.setCoast();
    }
}