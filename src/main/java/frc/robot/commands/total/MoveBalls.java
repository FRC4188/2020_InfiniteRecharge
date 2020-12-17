package frc.robot.commands.total;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Turret;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

public class MoveBalls extends CommandBase {

    Turret turret;
    Magazine magazine;
    Intake intake;
    Limelight limelight;
    Shooter shooter;

    DoubleSupplier turretPowSup;
    BooleanSupplier manualSup;
    BooleanSupplier shootingSup;
    BooleanSupplier intakingSup;
    BooleanSupplier outtakingSup;

    double TOLERANCE = 25;

    public MoveBalls(Turret turret, Magazine magazine, Intake intake, Limelight limelight, Shooter shooter,
    DoubleSupplier turretPow, BooleanSupplier manual, BooleanSupplier shooting, BooleanSupplier intaking, BooleanSupplier outtaking) {
        addRequirements(turret, magazine, intake, shooter);
        
        this.turret = turret;
        this.magazine = magazine;
        this.intake = intake;
        this.shooter = shooter;
        this.limelight = limelight;

        this.turretPowSup = turretPow;
        this.intakingSup = intaking;
        this.outtakingSup = outtaking;
        this.shootingSup = shooting;
        this.manualSup = manual;
    }

    @Override
    public void initialize() {
        // nothing is called on initialization.
    }

    @Override
    public void execute() {
        boolean intaking = this.intakingSup.getAsBoolean();
        boolean outtaking = this.outtakingSup.getAsBoolean();
        boolean shooting = this.shootingSup.getAsBoolean();
        boolean manual = this.manualSup.getAsBoolean();
        double turretPow = this.turretPowSup.getAsDouble();
        boolean mid = magazine.midBeamClear();
        boolean top = magazine.topBeamClear();

        boolean aimed = limelight.getIsAimed();
        boolean ready = aimed && Math.abs(shooter.getRightVelocity() - limelight.formulaRpm()) < TOLERANCE;

        if (shooting && !manual) {
            shoot(ready, mid, top);
        } else if (intaking) {
            intake(mid, top);
        } else if (outtaking) {
            outtake(mid, top);
        } else if (manual) {
            manual(turretPow, shooting);
        } else {
            magazine.set(0.0);
            intake.spin(0.0, 0.0);
            turret.set(0.0);
            shooter.setVelocity(3500.0);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        magazine.set(0.0);
        intake.spin(0.0, 0.0);
        turret.set(0.0);
        shooter.setVelocity(3500.0);
    }

    private void shoot(boolean ready, boolean mid, boolean top) {
        if(limelight.hasTarget() == 1.0) {
            turret.trackTarget(limelight.getHorizontalAngle());
            shooter.setVelocity(limelight.formulaRpm());
        } else {
            turret.set(0.0);
            shooter.setVelocity(3500.0);
        }                
        if (!mid && top) {
                magazine.set(0.35);
                intake.spin(-0.5,0.0);
            }
        else if (mid && top) {
            magazine.set(0.35);
            intake.spin(-0.5,-1.0);
        }
        else if (ready) magazine.set(0.75);
        else {
            magazine.set(0.0);
            intake.spin(-0.0,0.0);
        }
    }

    private void manual(double turretPow, boolean shooting) {
        turret.set(turretPow);
        magazine.set((shooting) ? 1.0 : 0.0);
        shooter.setVelocity((limelight.hasTarget() == 1.0) ? limelight.formulaRpm() : 3500.0);
    }

    private void intake(boolean mid, boolean top){
        if (!mid && top) {
            intake.spin(-1.0, -1.0);
            magazine.set(0.35);
        }
        else if (mid && top) {
            magazine.set(0.0);
            intake.spin(-1.0, -1.0);
        }
        else {
            magazine.set(0.0);
            intake.spin(-1.0,0.0);
        }
    }

    private void outtake(boolean mid, boolean top){
        if (!mid && top) {
            intake.spin(1.0, 1.0);
            magazine.set(-0.1);
        }
        else if (mid && top) {
            magazine.set(0.0);
            intake.spin(1.0, 0.5);
        }
        else {
            magazine.set(-0.35);
            intake.spin(1.0,0.0);
        }
    }
}