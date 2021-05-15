package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeCamera;
import java.util.function.DoubleSupplier;

/**
 * Drives forward a certain percent while keeping the drivetrain centered on the vision target.
 */
public class CenterBall extends CommandBase {

    // initialize class variables for subsystems and input.
    private final Drivetrain drivetrain;
    private final IntakeCamera intakecam;
    private final DoubleSupplier percentForward;

    // instantiate a PID controller to control the rotation of the robot.
    private final double kP = 0.015;
    private final double kD = 0.7;
    private final PIDController pid = new PIDController(kP, 0, kD);

    /**
     * Constructs a new CenterBay command to drive forward a certain percent while keeping
     * the drivetrain centered on the vision target.
     *
     * @param drivetrain - Drivetrain subsystem to require.
     * @param intakecam - Camera subsystem to use.
     * @param percentForward - Supplier of percent to drive forward [-1.0, 1.0].
     */
    public CenterBall(Drivetrain drivetrain, IntakeCamera intakecam,
            DoubleSupplier percentForward) {
        // Require the drivetrain for this command so that it is the only one controlling the drivetrain when it is running.
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
        this.intakecam = intakecam;
        this.percentForward = percentForward;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        // Set the forward/reverse motion of the drivetrain equal to the driver input.
        double xSpeed = percentForward.getAsDouble();
        // Set the rotation of the robot equal to the PID Controller output (calculated from X angle of the closest ball).
        double zRotation = pid.calculate(intakecam.getCloseX(0.0), 0);
        drivetrain.arcade(xSpeed, zRotation, false);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }

}
