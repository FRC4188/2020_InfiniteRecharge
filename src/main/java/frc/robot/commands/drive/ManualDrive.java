package frc.robot.commands.drive;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.SlewRateLimiter;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.CspController;
import frc.robot.utils.CspController.Scaling;

/**
 * Manually controls drivetrain using arcade model.
 */
public class ManualDrive extends CommandBase {

    private final Drivetrain drivetrain;

    /*
    private DoubleSupplier xSpeedSupplier;
    private DoubleSupplier zRotationSupplier;
    private BooleanSupplier fineControlSupplier;
    */

    private DoubleSupplier speed;
    private DoubleSupplier rotation;
    private BooleanSupplier fine;
    private SlewRateLimiter speedLimiter = new SlewRateLimiter(1.5);
    private SlewRateLimiter rotLimiter = new SlewRateLimiter(1.5);


    /**
     * Constructs a new ManualDrive command to control drivetrain.
     *
     * @param drivetrain - Drivetrain subsystem to require.
     * @param pilot - Pilot controller object.
     * @param copilot - Copilot controller object.
     */
    public ManualDrive(Drivetrain drivetrain, DoubleSupplier speed, DoubleSupplier rotation, BooleanSupplier fine) {
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
        this.speed = speed;
        this.rotation = rotation;
        this.fine = fine;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        // take controller input
        boolean PfineControl = fine.getAsBoolean();
        double PxSpeed = speed.getAsDouble();
        double PzRotation = -rotation.getAsDouble();

        double SetSpeed;
        double SetRotation;

        
        // modify output based on fine control boolean
        SetSpeed = (PfineControl) ? PxSpeed * 0.75 : PxSpeed;
        SetRotation = (PfineControl) ? PzRotation * 0.75 : PzRotation;
        
        
        SetSpeed = speedLimiter.calculate(SetSpeed);
        SetRotation = rotLimiter.calculate(SetRotation);

        //drivetrain.arcade(SetSpeed, SetRotation);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
