package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/**
 * Manually controls drivetrain using arcade model.
 */
public class ManualDrive extends CommandBase {

    private final Drivetrain drivetrain;

    private DoubleSupplier xSpeedSupplier;
    private DoubleSupplier zRotationSupplier;
    private BooleanSupplier fineControlSupplier;

    private static final double SPEED_CONST = 0.5;
    private static final double ROTATION_CONST = 0.5;

    private SlewRateLimiter speedLimiter = new SlewRateLimiter(3);
    private SlewRateLimiter rotLimiter = new SlewRateLimiter(3);


    /**
     * Constructs a new ManualDrive command to control drivetrain.
     *
     * @param drivetrain - Drivetrain subsystem to require.
     * @param xSpeed - Forward speed of robot [-1.0, 1.0].
     * @param zRotation - Rotation rate of robot [-1.0, 1.0].
     * @param fineControl - If true, slows driving.
     */
    public ManualDrive(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier zRotation,
            BooleanSupplier fineControl) {
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
        this.xSpeedSupplier = xSpeed;
        this.zRotationSupplier = zRotation;
        this.fineControlSupplier = fineControl;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {

        // get values from suppliers and limit rate
        boolean fineControl = fineControlSupplier.getAsBoolean();
        double xSpeed = speedLimiter.calculate(xSpeedSupplier.getAsDouble());
        double zRotation = rotLimiter.calculate(zRotationSupplier.getAsDouble());

        // modify output based on fine control boolean
        xSpeed = (fineControl) ? xSpeed * SPEED_CONST : xSpeed;
        zRotation = (fineControl) ? zRotation * ROTATION_CONST : zRotation;

        // command motor output
        drivetrain.arcade(xSpeed, zRotation);

    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
