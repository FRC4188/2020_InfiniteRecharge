package frc.robot.commands.drive;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.utils.CSPMath;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** Drives forward using pilot left joystick while
 *  turning to keep bay centered. */
public class CenterBay extends CommandBase {

    Drivetrain drivetrain;
    Limelight limelight;
    double output;

    final double kP = 0.02;
    final double kD = 0.01;
    final double DELTA_T = 0.02;

    double lastError;

    public CenterBay(Drivetrain drivetrain, Limelight limelight, double output) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.output = output;
        addRequirements(drivetrain, limelight);
    }

    @Override
    public void initialize() {
        limelight.trackTarget();
    }

    @Override
    public void execute() {

        // use pilot left joystick for xSpeed
        double xSpeed = output;

        // angle p loop
        double angleErr = limelight.getHorizontalAngle();
        double turnDeriv = (angleErr - lastError) * DELTA_T;
        double zTurn = kP * angleErr + kD * turnDeriv;
        zTurn = CSPMath.constrainKeepSign(zTurn, 0.05, 1.0);
        lastError = angleErr;

        // command motor output
        drivetrain.arcade(xSpeed, zTurn);

    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }

}