package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.SlewRateLimiter;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.CspController;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

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

    private CspController pilot;
    private CspController copilot;

    private SlewRateLimiter speedLimiter = new SlewRateLimiter(1.5);
    private SlewRateLimiter rotLimiter = new SlewRateLimiter(1.5);


    /**
     * Constructs a new ManualDrive command to control drivetrain.
     *
     * @param drivetrain - Drivetrain subsystem to require.
     * @param pilot - Pilot controller object.
     * @param copilot - Copilot controller object.
     */
    public ManualDrive(Drivetrain drivetrain, CspController pilot, CspController copilot) {
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
        this.pilot = pilot;
        this.copilot = copilot;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        // take controller input
        boolean PfineControl = pilot.getBumper(Hand.kLeft);
        double PxSpeed = pilot.getY(Hand.kLeft);
        double PzRotation = pilot.getX(Hand.kRight);
        double CxSpeed = copilot.getY(Hand.kLeft);
        double CzRotation = copilot.getX(Hand.kRight);

        double SetSpeed;
        double SetRotation;

        if (PxSpeed != 0.0 || PzRotation != 0.0) {
            // modify output based on fine control boolean
            PxSpeed = (PfineControl) ? PxSpeed : PxSpeed;
            PzRotation = (PfineControl) ? PzRotation : PzRotation;

            // Apply a slew to the motor input.
            SetSpeed = PxSpeed;
            SetRotation = -PzRotation;

        } else if (CxSpeed != 0.0 || CzRotation != 0.0) {
            //apply a slew to motor output
            SetSpeed = CxSpeed;
            SetRotation = -CzRotation;

        } else {
            SetSpeed = 0.0;
            SetRotation = 0.0;
        }
        
        SetSpeed = speedLimiter.calculate(SetSpeed);
        SetRotation = rotLimiter.calculate(SetRotation);

        drivetrain.arcade(SetSpeed, SetRotation);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
