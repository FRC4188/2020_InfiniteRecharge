package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.AutoCenterBay;
import frc.robot.commands.drive.CenterBay;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.commands.magazine.AutoBelt;
import frc.robot.commands.magazine.TurnBelt;
import frc.robot.commands.shooter.CancelShooter;
import frc.robot.commands.shooter.RunShooter;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
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

    // controller initialization
    private final CspController pilot = new CspController(0);
    private final CspController copilot = new CspController(1);

    // command groups
    ParallelCommandGroup shoot = new ParallelCommandGroup(new SpinShooter(shooter, limelight), 
        new AutoBelt(magazine));
    ParallelCommandGroup cancelShoot = new ParallelCommandGroup(new TurnBelt(magazine, 0), 
        new CancelShooter(shooter));
    //ParallelRaceGroup timedShoot = new ParallelRaceGroup(new Wait(), shoot);
    SequentialCommandGroup autoShoot = new SequentialCommandGroup(new AutoCenterBay(drivetrain, 
        limelight, 0.1), shoot);

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
        pilot.getYButtonObj().whileHeld(shoot);
        pilot.getYButtonObj().whenReleased(cancelShoot);
        pilot.getRbButtonObj().whileHeld(new CenterBay(drivetrain, limelight, pilot.getY(Hand.kLeft)));
        //pilot.getLbButtonObj().whenPressed(autoShoot);
        pilot.getStartButtonObj().whenPressed(new KillAll());
    }

    public static Limelight getLimelight(){
        return limelight;
    }

    public static Shooter getShooter(){
        return shooter;
    }
}
