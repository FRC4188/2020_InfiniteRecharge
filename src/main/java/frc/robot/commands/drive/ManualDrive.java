package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import java.util.function.DoubleSupplier;

/**
 * Manually controls drivetrain using pilot controller.
 */
public class ManualDrive extends CommandBase {

    private final Drivetrain drivetrain;
    private DoubleSupplier xSpeed;
    private DoubleSupplier zRotation;

    public ManualDrive(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier zRotation) {
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
        this.xSpeed = xSpeed;
        this.zRotation = zRotation;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        drivetrain.arcade(xSpeed.getAsDouble(), zRotation.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
