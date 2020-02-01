package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.commands.drive.CenterBay;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.commands.magazine.TurnBelt;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.CspController;

/**
 * Class containing setup for robot.
 */
public class RobotContainer {

    // subsystem initialization
    private final Drivetrain drivetrain = new Drivetrain();
    private final Magazine magazine = new Magazine();
    private final Shooter shooter = new Shooter();
    private final Limelight limelight = new Limelight();

    // controller initialization
    private final CspController pilot = new CspController(0);
    private final CspController copilot = new CspController(1);

    /**
     * Initializes robot subsystems, controllers, and commands.
     */
    public RobotContainer() {
        setDefaultCommands();
        configureButtonBindings();
    }

    /**
     * Resets variables and sensors for each subsystem.
     */
    public void resetSubsystems() {
        drivetrain.reset();
    }

    /**
     * Sets the default command for each subsystem, if applicable.
     */
    private void setDefaultCommands() {
        drivetrain.setDefaultCommand(new ManualDrive(drivetrain,
                () -> pilot.getY(Hand.kLeft),
                () -> pilot.getX(Hand.kRight),
                () -> pilot.getBumper(Hand.kLeft)
        ));
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {
        pilot.getBButtonObj().whileHeld(new TurnBelt(magazine, 0.9));
        pilot.getAButtonObj().whileHeld(new SpinShooter(shooter, limelight.formulaRPM()));
        pilot.getRbButtonObj().whileHeld(new CenterBay(drivetrain, limelight, pilot.getY(Hand.kLeft)));
    }

}
