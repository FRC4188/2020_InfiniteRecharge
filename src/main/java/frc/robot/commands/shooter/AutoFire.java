package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake;

public class AutoBallCycle extends CommandBase {

    private Shooter shooter;
    private Limelight limelight;
    private boolean aimed; 
    private double diff;
    private Magazine magazine;
    private Intake intake;

    private boolean top;
    private boolean mid;
    private boolean cont;
    
    public AutoBallCycle(Shooter shooter, Limelight limelight) {
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
        aimed = limelight.getIsAimed();
        diff = shooter.getLeftVelocity() - limelight.formulaRpm();
        top = magazine.topBeamClear();
        mid = magazine.midBeamClear();

        if (cont) {
                if (!mid && top) {
                    magazine.set(0.35);
                    intake.spin(0,0);
                }
                else if (mid && top) {
                    magazine.set(0.35);
                    intake.spin(0,0.5);
                }
                if (!top && (diff < 75 && diff > -75) && aimed) magazine.set(1.0);
                else magazine.set(0);
        }else {
            magazine.set(0.0);
            intake.spin(0,0);
        }
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