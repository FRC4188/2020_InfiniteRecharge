package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Intake;

public class AutoFireQuantity extends CommandBase {

    private Shooter shooter;
    private Limelight limelight;
    private Magazine magazine;
    private Intake intake;
    private Turret turret;

    private int quantity;

    private double THRESHOLD = 250.0;

    private boolean lastTop = true;
    
    public AutoFireQuantity(Shooter shooter, Turret turret, Magazine magazine2, Intake intake, Limelight limelight, int quantity) {
        addRequirements(shooter, turret, magazine2, intake);
        this.shooter = shooter;
        this.intake = intake;
        this.limelight = limelight;
        this.magazine = magazine2;
        this.turret = turret;
        this.quantity = quantity;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setVelocity(limelight.formulaRpm());
        boolean aimed = limelight.getIsAimed() && (limelight.hasTarget());
        double diff = shooter.getLeftVelocity() - (limelight.formulaRpm());
        boolean ready = (aimed && (Math.abs(diff) < THRESHOLD));
        boolean top = magazine.topBeamClear();
        boolean entry = magazine.entryBeamClear();

        if(limelight.hasTarget()) {
            turret.trackTarget(limelight.getHorizontalAngle());
        } else {
            turret.set(0);
        }                

        if (ready) magazine.set(1.0);
        else if (top) magazine.set(0.25);
        else magazine.set(0.0);

        if (ready || (top && entry)) intake.spin(-0.5, -1.0);
        else intake.spin(-0.5, 0.0);

        if (!lastTop && top) quantity--;

        lastTop = top;
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
            shooter.set(3500);
        }

    }

}