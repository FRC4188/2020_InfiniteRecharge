package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

public class Spin360 extends CommandBase{
    private Turret turret;
    private Limelight limelight;
    private double position, tolerance, counter, targetPosition, lastError;
    private final double kP = 0.000005;
    private final double kD = 0.75;
    private final double DELTA_T = 0.02;
    
    public Spin360(Turret turret, Limelight limelight, double tolerance) {
        addRequirements(turret);
        this.turret = turret;
        this.limelight = limelight;
        this.tolerance = tolerance;
    }
    
    @Override
    public void initialize() {
        targetPosition = turret.getPosition() + limelight.getHorizontalAngle() - 360 * 
                Math.signum(turret.getPosition() - 180);
    }

    @Override
    public void execute() {
        position = turret.getPosition();
        double error = targetPosition - position;
        turret.set(kP * error + kD * lastError * DELTA_T);
        lastError = error;
        if (Math.abs(error) < tolerance) counter++;
        else counter = 0;
    }

    @Override
    public boolean isFinished() {
        return counter > 10;
    }

    @Override
    public void end(boolean interrupted) {
        turret.set(0);
    }

}
