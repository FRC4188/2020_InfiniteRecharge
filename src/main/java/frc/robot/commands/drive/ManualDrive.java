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

    private static final double SPEED_CONST = 0.2;
    private static final double ROTATION_CONST = 0.3;

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
        boolean CfineControl = copilot.getYButton();
        double CxSpeed = copilot.getY(Hand.kLeft);
        double CzRotation = copilot.getX(Hand.kRight);

        if (PxSpeed != 0.0 || PzRotation != 0.0) {
            // modify output based on fine control boolean
            PxSpeed = (PfineControl) ? PxSpeed * SPEED_CONST : PxSpeed;
            PzRotation = (PfineControl) ? PzRotation * ROTATION_CONST : PzRotation;

            // Apply a slew to the motor input.
            PxSpeed = speedLimiter.calculate(PxSpeed);
            PzRotation = rotLimiter.calculate(PzRotation);

            // command motor output
            drivetrain.arcade(PxSpeed, -PzRotation);

        } else if (CxSpeed != 0.0 || CzRotation != 0.0) {
            // modify output based on fine control boolean
            CxSpeed = (CfineControl) ? CxSpeed * SPEED_CONST : CxSpeed;
            CzRotation = (CfineControl) ? CzRotation * ROTATION_CONST : CzRotation;

            //apply a slew to motor output
            CxSpeed = speedLimiter.calculate(CxSpeed);
            CzRotation = rotLimiter.calculate(CzRotation);

            // command motor output
            drivetrain.arcade(CxSpeed, -CzRotation);

        } else drivetrain.arcade(0,0);
        

    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
