package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

public class Spin360 extends CommandBase{
    private Turret turret;
    private double position, tolerance, counter, trim, targetPosition;
    
    public Spin360(Turret turret, double tolerance) {
        addRequirements(turret);
        this.turret = turret;
        this.tolerance = tolerance;
        if (turret.getPosition() < -170 || turret.getPosition() > 170)
            targetPosition = turret.getPosition() - 360 * Math.signum(turret.getPosition());
        else targetPosition = turret.getPosition();
    }
    
    @Override
    public void initialize() {
        SmartDashboard.putNumber("Turret trim", trim);
    }

    @Override
    public void execute() {
        position = turret.getPosition();
        trim = SmartDashboard.getNumber("Turret trim", 0);
        turret.turretToAngle(position + trim, tolerance);
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