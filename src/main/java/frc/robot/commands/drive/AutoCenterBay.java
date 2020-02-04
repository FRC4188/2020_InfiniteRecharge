package frc.robot.commands.drive;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.utils.CSPMath;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoCenterBay extends CommandBase {

    Drivetrain drivetrain;
    Limelight limelight;
    double tolerance, counter = 0;

    final double kP = 0.02;
    final double kD = 0.01;
    final double DELTA_T = 0.02;

    double lastError;

    public AutoCenterBay(Drivetrain drivetrain, Limelight limelight, double tolerance) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.tolerance = tolerance;
        addRequirements(drivetrain, limelight);
    }

    @Override
    public void initialize() {
        limelight.trackTarget();
    }

    @Override
    public void execute() {

        // angle p loop
        double angleErr = limelight.getHorizontalAngle();
        double turnDeriv = (angleErr - lastError) * DELTA_T;
        double zTurn = kP * angleErr + kD * turnDeriv;
        zTurn = CSPMath.constrainKeepSign(zTurn, 0.05, 1.0);
        lastError = angleErr;
        if (Math.abs(lastError) < tolerance) counter++;
        else counter = 0;

        // command motor output
        drivetrain.arcade(0, zTurn);
    }

    @Override
    public boolean isFinished() {
        return counter > 10;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.arcade(0, 0);
    }

}