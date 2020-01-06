package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

/**
 * Drives to a given distance in feet using PID loop. All distances relative to
 * starting position.
 */
public class DriveToDistance extends CommandBase {

    private final Drivetrain drivetrain;
    private final double kP = 0.3;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private PIDController leftPidC = new PIDController(kP, kI, kD);
    private PIDController rightPidC = new PIDController(kP, kI, kD);
    private double distance;
    private double tolerance;
    private double leftSetpoint;
    private double rightSetpoint;

    public DriveToDistance(Drivetrain d, double distance, double tolerance) {
        drivetrain = d;
        this.distance = distance;
        this.tolerance = tolerance;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        leftSetpoint = drivetrain.getLeftPosition() + distance;
        rightSetpoint = drivetrain.getRightPosition() + distance;
        leftPidC.setTolerance(tolerance);
        rightPidC.setTolerance(tolerance);
    }

    @Override
    public void execute() {
        drivetrain.setLeft(leftPidC.calculate(drivetrain.getLeftPosition(), leftSetpoint));
        drivetrain.setRight(rightPidC.calculate(drivetrain.getRightPosition(), rightSetpoint));
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.setLeft(0);
        drivetrain.setRight(0);
        leftPidC.reset();
        rightPidC.reset();
    }

    @Override
    public boolean isFinished() {
        return leftPidC.atSetpoint() && rightPidC.atSetpoint();
    }

}