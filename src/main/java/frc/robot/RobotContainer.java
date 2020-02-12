package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.DriveCenterPort;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.shooter.SpinShooterFormula;
import frc.robot.commands.turret.ManualTurret;
import frc.robot.commands.turret.TurretCenterPort;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utils.CspController;
import frc.robot.utils.KillAll;

/**
 * Class containing setup for robot.
 */
public class RobotContainer {

    // subsystem initialization
    private final Drivetrain drivetrain = new Drivetrain();
    private final Magazine magazine = new Magazine();
    private final Shooter shooter = new Shooter();
    private final Limelight limelight = new Limelight();
    private final Turret turret = new Turret();

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
        shooter.setDefaultCommand(new SpinShooterFormula(shooter, limelight));
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {

        pilot.getBButtonObj().whileHeld(new RunMagazine(magazine, 0.9));
        pilot.getBButtonObj().whenReleased(new RunMagazine(magazine, 0));

        pilot.getRbButtonObj().whileHeld(new DriveCenterPort(
                drivetrain, limelight, () -> pilot.getY(Hand.kLeft)
        ));

        pilot.getStartButtonObj().whenPressed(new KillAll());

        pilot.getDpadLeftButtonObj().whileHeld(new ManualTurret(turret, -0.5));
        pilot.getDpadLeftButtonObj().whenReleased(new ManualTurret(turret, 0));
        pilot.getDpadRightButtonObj().whileHeld(new ManualTurret(turret, 0.5));
        pilot.getDpadRightButtonObj().whenReleased(new ManualTurret(turret, 0));

        pilot.getXButtonObj().whenPressed(new TurretCenterPort(turret, limelight));

    }

}
