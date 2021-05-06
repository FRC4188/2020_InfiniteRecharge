package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;

public class AutoFire extends CommandBase {

    private Limelight limelight;
    private Drivetrain drivetrain;
    private Magazine magazine;
    private Intake intake;
    private Turret turret;
    private Shooter shooter;
    private boolean cont;

    private double THRESHOLD = 100.0;
    private double drivetrainMagnitude = 0;
    private double resultingMagnitude = 0;
    private double turretAngle = 0;
    

    public AutoFire(Drivetrain drivetrain, Shooter shooter, Magazine magazine, Intake intake, Limelight limelight, Turret turret, boolean cont) {
        addRequirements(shooter, magazine, intake, turret);
        this.shooter = shooter;
        this.intake = intake;
        this.limelight = limelight;
        this.magazine = magazine;
        this.turret = turret;
        this.drivetrain = drivetrain;
        this.cont = cont;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        drivetrainMagnitude = (drivetrain.getLeftVelocity() + drivetrain.getLeftVelocity()) / 2; //meters per second
        resultingMagnitude = (0.0762 * Math.PI * limelight.formulaRpm()) / 30; //meters per second
        turretAngle = (turret.getPosition() > -1) ? 180 - turret.getPosition() : 180 + turret.getPosition();
    
        double angleA = Math.asin(Math.sin(Math.toRadians(turretAngle)) * drivetrainMagnitude / resultingMagnitude);
        double angleB = 180 - turretAngle - Math.toDegrees(angleA);
        
        double shooterMagnitude = (Math.sin(Math.toRadians(angleB)) * resultingMagnitude) / Math.sin(Math.toRadians(turretAngle)); //meters per second
    
        double movingRPM = (30 * shooterMagnitude) / (0.0762 * Math.PI);

        double setRPM = (((drivetrain.getLeftVelocity() + drivetrain.getRightVelocity()) / 2) > 0.2) ? movingRPM : limelight.formulaRpm();
        shooter.setVelocity(setRPM);

        boolean seen = limelight.hasTarget();
        boolean aimed = (limelight.getIsAimed() && seen);
        double diff = shooter.getLeftVelocity() - (setRPM);
        boolean ready = (aimed && (Math.abs(diff) < THRESHOLD));
        boolean top = magazine.topBeamClear();
        boolean mid = magazine.midBeamClear();
        boolean entry = magazine.entryBeamClear();

        SmartDashboard.putNumber("Formula RPM", setRPM);

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
        shooter.setVelocity(2000.0);
    }
}