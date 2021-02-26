package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Intake;

public class AutoFire extends CommandBase {

    private Limelight limelight;
    private Magazine magazine;
    private Intake intake;
    private Turret turret;
    private Shooter shooter;
    private boolean cont;
    private double THRESHOLD = 250.0;

    public AutoFire(Shooter shooter, Magazine magazine, Intake intake, Limelight limelight, Turret turret, boolean cont) {
        addRequirements(shooter, magazine, intake, turret);
        this.shooter = shooter;
        this.intake = intake;
        this.limelight = limelight;
        this.magazine = magazine;
        this.turret = turret;
        this.cont = cont;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setVelocity(limelight.formulaRpm());
        boolean aimed = limelight.getIsAimed() && (limelight.hasTarget() == 1.0);
        double diff = shooter.getLeftVelocity() - (limelight.formulaRpm());
        boolean ready = aimed && (Math.abs(diff) < THRESHOLD);
        boolean top = magazine.topBeamClear();
        boolean entry = magazine.entryBeamClear();

        if(limelight.hasTarget() == 1.0) {
            turret.trackTarget(limelight.getHorizontalAngle());
        } else {
            turret.set(0);
        }                

        if (ready) magazine.set(1.0);
        else if (top) magazine.set(0.25);
        else magazine.set(0.0);

        if (ready || (top && entry)) intake.spin(-0.5, -1.0);
        else intake.spin(-0.5, 0.0);
    }

    @Override
    public boolean isFinished() {
        return !cont;
    }

    @Override
    public void end(boolean interrupted) {
        turret.set(0.0);
        magazine.set(0.0);
        intake.spin(0.0, 0.0);
        shooter.set(3500.0);
    }
}