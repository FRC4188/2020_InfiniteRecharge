package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Intake;

public class AutoFire extends CommandBase {

    private Limelight limelight;
    private boolean aimed; 
    private double diff;
    private Magazine magazine;
    private Intake intake;
    private Turret turret;
    private Shooter shooter;

    private boolean top;
    private boolean mid;
    private boolean cont;

    private double adjust;
    
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
        if(intake.isRaised()) intake.lower();
    }

    @Override
    public void execute() {
        shooter.setVelocity(limelight.formulaRpm());
        aimed = limelight.getIsAimed();
        diff = shooter.getLeftVelocity() - limelight.formulaRpm();
        top = magazine.topBeamClear();
        mid = magazine.midBeamClear();

        if (cont) {
            if(limelight.hasTarget() == 1.0) {
                if((limelight.getSkew() < 24) || limelight.getSkew() > -24) adjust = SmartDashboard.getNumber("Turret Aim adjust", -3.0) - limelight.getOffset();
                else adjust = SmartDashboard.getNumber("Turret Aim adjust", -2.0);
                turret.set((-limelight.getHorizontalAngle() + adjust - limelight.getOffset()) / 47.0);
                turret.setTracking(true);
            } else {
                turret.set(0);
            }                
            if (!mid && top) {
                    magazine.set(0.35);
                    intake.spin(-0.5,0);
                }
                else if (mid && top) {
                    magazine.set(0.35);
                    intake.spin(-0.5,-1.0);
                }
                else if (!top && (diff < 100 && diff > -100) && aimed) magazine.set(1.0);
                else {
                    magazine.set(0);
                    intake.spin(-0.,0.0);
                }
        }else {
            magazine.set(0.0);
            intake.spin(0,0);
            turret.set(0);
        }
    }

    @Override
    public boolean isFinished() {
        return !cont;
    }

    @Override
    public void end(boolean interrupted) {
        if(!interrupted) {
            magazine.set(0);
            intake.spin(0,0);
            shooter.set(3000);
        }

        turret.setTracking(false);
    }

}