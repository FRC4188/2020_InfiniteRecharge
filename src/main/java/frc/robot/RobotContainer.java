package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.AutoCenterBay;
import frc.robot.commands.drive.CenterBay;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.commands.groups.CancelShoot;
import frc.robot.commands.groups.Shoot;
import frc.robot.commands.magazine.AutoBelt;
import frc.robot.commands.magazine.TurnBelt;
import frc.robot.commands.shooter.CancelShooter;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.ManualTurret;
import frc.robot.commands.turret.Spin360;
import frc.robot.commands.turret.TurnTurret;
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
    private static final Shooter shooter = new Shooter();
    private static final Limelight limelight = new Limelight();
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
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {
        pilot.getBButtonObj().whileHeld(new TurnBelt(magazine, 0.9));
        pilot.getBButtonObj().whenReleased(new TurnBelt(magazine, 0));
        pilot.getAButtonObj().whileHeld(new SpinShooter(shooter, limelight));
        pilot.getAButtonObj().whenReleased(new CancelShooter(shooter));
        pilot.getYButtonObj().whileHeld(new Shoot(shooter, limelight, magazine));
        pilot.getYButtonObj().whenReleased(new CancelShoot(shooter, limelight, magazine));
        pilot.getRbButtonObj().whileHeld(new CenterBay(drivetrain, limelight, pilot.getY(Hand.kLeft)));
        pilot.getStartButtonObj().whenPressed(new KillAll());
        pilot.getDpadLeftButtonObj().whileHeld(new ManualTurret(turret, -0.5));
        pilot.getDpadLeftButtonObj().whenReleased(new ManualTurret(turret, 0));
        pilot.getDpadRightButtonObj().whileHeld(new ManualTurret(turret, 0.5));
        pilot.getDpadRightButtonObj().whenReleased(new ManualTurret(turret, 0));
        pilot.getXButtonObj().whileHeld(new TurnTurret(turret, limelight, 0.5));
        pilot.getLbButtonObj().whenPressed(new Spin360(turret, 1));
    }

    public static Limelight getLimelight(){
        return limelight;
    }

    public static Shooter getShooter(){
        return shooter;
    }
}
