package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import java.util.function.DoubleSupplier;

/**
 * Drives forward a certain percent while keeping the drivetrain centered on the vision target.
 */
public class DriveCenterPort extends CommandBase {

    private final Drivetrain drivetrain;
    private final Limelight limelight;
    private final DoubleSupplier percentForward;

    private final double kP = 0.05;
    private final double kD = 0.4;
    private final PIDController pid = new PIDController(kP, 0, kD);

    /**
     * Constructs a new CenterBay command to drive forward a certain percent while keeping
     * the drivetrain centered on the vision target.
     *
     * @param drivetrain - Drivetrain subsystem to require.
     * @param limelight - Limelight subsystem to use.
     * @param percentForward - percent to drive forward [-1.0, 1.0].
     */
    public DriveCenterPort(Drivetrain drivetrain, Limelight limelight,
            DoubleSupplier percentForward) {
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.percentForward = percentForward;
    }

    @Override
    public void initialize() {
        limelight.trackTarget();
    }

    @Override
    public void execute() {
        double xSpeed = percentForward.getAsDouble();
        double zRotation = pid.calculate(-limelight.getHorizontalAngle(), 0);
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
