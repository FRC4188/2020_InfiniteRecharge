package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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
    private double THRESHOLD = 100.0;
    

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
        boolean seen = limelight.hasTarget();
        boolean aimed = (limelight.getIsAimed() && seen);
        double diff = shooter.getLeftVelocity() - (limelight.formulaRpm());
        boolean ready = (aimed && (Math.abs(diff) < THRESHOLD));
        boolean top = magazine.topBeamClear();
        boolean mid = magazine.midBeamClear();
        boolean entry = magazine.entryBeamClear();

        if(seen) {
            turret.trackTarget(limelight.getHorizontalAngle());
        } else {
            turret.set(0);
        }                

        /*if (ready) magazine.set(1.0);
        else if (!top) magazine.set(0.25);
        else magazine.set(0.0);

        if (ready || (top && entry)) intake.spin(-0.5, 1.0);
        else intake.spin(-0.5, -1.0);*/

        if (ready && magazine.getBallInMagazine()) {
            magazine.set(0.75);
        } else if (!magazine.getBallInMagazine()) {
            magazine.set(0.5);
            intake.spin(-1.0, -0.5);
        } else {
            new WaitCommand(1.0);
            magazine.set(0.0);
            intake.spin(0.0, 0.0);
        }
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
        shooter.set(2000.0);
    }
}