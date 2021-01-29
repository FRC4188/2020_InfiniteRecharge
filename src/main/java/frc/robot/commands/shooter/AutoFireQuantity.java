package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake;

public class AutoFireQuantity extends CommandBase {

    private Shooter shooter;
    private Limelight limelight;
    private boolean aimed; 
    private double diff;
    private Magazine magazine;
    private Intake intake;

    private boolean top;
    private boolean lasttop;
    private boolean mid;

    private int quantity;
    
    public AutoFireQuantity(Shooter shooter, Magazine magazine, Intake intake, Limelight limelight, int quantity) {
        addRequirements(shooter, magazine, intake);
        this.shooter = shooter;
        this.intake = intake;
        this.limelight = limelight;
        this.magazine = magazine;
        this.quantity = quantity;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        top = magazine.topBeamClear();
        if(!lasttop&&top) quantity --;

        shooter.setVelocity(limelight.formulaRpm());
        aimed = limelight.getIsAimed();
        diff = shooter.getLeftVelocity() - limelight.formulaRpm();
        mid = magazine.midBeamClear();
        lasttop=top;

        if (!(quantity==0)) {
                if (!mid && top) {
                    magazine.set(0.35);
                    intake.spin(0,0);
                }
                else if (mid && top) {
                    magazine.set(0.35);
                    intake.spin(0,0.5);
                }
                else if (!top && (diff < 75 && diff > -75) && aimed) magazine.set(1.0);
                else {
                    magazine.set(0);
                    intake.spin(0,0);
                }
        }else {
            magazine.set(0.0);
            intake.spin(0,0);
        }
    }

    @Override
    public boolean isFinished() {
        return (quantity==0);
    }

    @Override
    public void end(boolean interrupted) {
        if(!interrupted) {
            magazine.set(0);
            intake.spin(0,0);
            shooter.set(3000);
        }

    }

}