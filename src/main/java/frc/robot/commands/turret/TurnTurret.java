package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

public class TurnTurret extends CommandBase{
    private Turret turret;
    private double position, tolerance, counter, trim, targetPosition;
    private Limelight limelight;
    
    public TurnTurret(Turret turret, Limelight limelight, double tolerance) {
        addRequirements(turret, limelight);
        this.turret = turret;
        this.limelight = limelight;
        this.tolerance = tolerance;
        targetPosition = limelight.getHorizontalAngle();
        if (targetPosition + turret.getPosition() < -170 || targetPosition + turret.getPosition() > 170) 
            targetPosition -= 360 * Math.signum(position);
    }
    
    @Override
    public void initialize() {
        SmartDashboard.putNumber("Turret trim", trim);
    }

    @Override
    public void execute() {
        position = turret.getPosition();
        trim = SmartDashboard.getNumber("Turret trim", 0);
        turret.turretToAngle(targetPosition + trim, tolerance);
        double error = targetPosition - position;
        if(Math.abs(error) < tolerance) counter++;
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