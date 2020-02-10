package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

public class MagazineDefault extends CommandBase{

    private Magazine magazine;
    private Shooter shooter;
    private Limelight limelight = new Limelight();

    public MagazineDefault(Magazine magazine, Shooter shooter) {
        addRequirements(magazine, limelight, shooter);
        this.magazine = magazine;
        this.shooter = shooter;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        double vel = shooter.getVelocity();
        double min = limelight.getMinBound();
        double max = limelight.getMaxBound();
        if (vel <= min || vel >= max) magazine.setSpeed(0);
        else magazine.setSpeed(0.9);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}