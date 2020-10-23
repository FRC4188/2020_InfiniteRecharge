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
     * @param xSpeed - Forward speed of robot [-1.0, 1.0].
     * @param zRotation - Rotation rate of robot [-1.0, 1.0].
     * @param fineControl - If true, slows driving.
     */
    public ManualDrive(Drivetrain drivetrain, CspController pilot, CspController copilot/*DoubleSupplier xSpeed, DoubleSupplier zRotation,
            BooleanSupplier fineControl*/) {
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
        this.pilot = pilot;
        this.copilot = copilot;
        /*
        this.xSpeedSupplier = xSpeed;
        this.zRotationSupplier = zRotation;
        this.fineControlSupplier = fineControl;
        */
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {

        /*
        // get values from suppliers and limit rate
        boolean fineControl = fineControlSupplier.getAsBoolean();
        double xSpeed = speedLimiter.calculate(xSpeedSupplier.getAsDouble());
        double zRotation = rotLimiter.calculate(zRotationSupplier.getAsDouble());
        */

        boolean PfineControl = pilot.getBumper(Hand.kLeft);
        double PxSpeed = pilot.getY(Hand.kLeft);
        double PzRotation = pilot.getX(Hand.kRight);
        boolean CfineControl = copilot.getYButton();
        double CxSpeed = copilot.getY(Hand.kLeft);
        double CzRotation = copilot.getX(Hand.kRight);

        /*
        // modify output based on fine control boolean
        xSpeed = (fineControl) ? xSpeed * SPEED_CONST : xSpeed;
        zRotation = (fineControl) ? zRotation * ROTATION_CONST : zRotation;

        //command motor output
        drivetrain.arcade(xSpeed, zRotation);
        */

        if (PxSpeed != 0.0 || PzRotation != 0.0) {
            // modify output based on fine control boolean
            PxSpeed = (PfineControl) ? PxSpeed * SPEED_CONST : PxSpeed;
            PzRotation = (PfineControl) ? PzRotation * ROTATION_CONST : PzRotation;

            PxSpeed = speedLimiter.calculate(PxSpeed);
            PzRotation = rotLimiter.calculate(PzRotation);

            // command motor output
            drivetrain.arcade(PxSpeed, -PzRotation);
        } else if (CxSpeed != 0.0 || CzRotation != 0.0) {
            // modify output based on fine control boolean
            CxSpeed = (CfineControl) ? CxSpeed * SPEED_CONST : CxSpeed;
            CzRotation = (CfineControl) ? CzRotation * ROTATION_CONST : CzRotation;

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
