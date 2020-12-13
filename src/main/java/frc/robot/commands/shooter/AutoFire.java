package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.controller.PIDController;
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

    public AutoFire(Shooter shooter, Magazine magazine, Intake intake, Limelight limelight, Turret turret) {
        addRequirements(shooter, magazine, intake, turret);
        this.shooter = shooter;
        this.intake = intake;
        this.limelight = limelight;
        this.magazine = magazine;
        this.turret = turret;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setVelocity(limelight.formulaRpm());
        aimed = limelight.getIsAimed() && (limelight.hasTarget() == 1.0);
        diff = shooter.getLeftVelocity() - (limelight.formulaRpm());
        top = magazine.topBeamClear();
        mid = magazine.midBeamClear();

            if(limelight.hasTarget() == 1.0) {
                turret.trackTarget(limelight.getHorizontalAngle());
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
            else if (!top && (diff < 50 && diff > -50) && aimed) magazine.set(0.75);
            else {
                magazine.set(0);
                intake.spin(-0.,0.0);
            }

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        turret.set(0.0);
        magazine.set(0.0);
        intake.spin(0.0, 0.0);
        shooter.set(3500.0);
    }
}