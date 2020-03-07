package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.commands.drive.DriveCenterPort;
import frc.robot.commands.drive.ManualDrive;
<<<<<<< HEAD
import frc.robot.commands.wheel.SpinRotations;
import frc.robot.commands.wheel.SpinColor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.NewWheel;
import frc.robot.subsystems.NewWheel.Color;
=======
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.shooter.SpinShooterFormula;
import frc.robot.commands.turret.AutoAim;
import frc.robot.commands.turret.ManualTurret;
import frc.robot.commands.turret.ZeroTurret;
import frc.robot.commands.vision.LimelightAsCamera;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
>>>>>>> 6b38d0c76c86587d631781dc85afae55046915ad
import frc.robot.utils.CspController;
import frc.robot.utils.KillAll;

/**
 * Class containing setup for robot.
 */
public class RobotContainer {

    // subsystem initialization
    private final Drivetrain drivetrain = new Drivetrain();
<<<<<<< HEAD
    private final NewWheel wheel = new NewWheel();
=======
    private final Magazine magazine = new Magazine();
    private final Shooter shooter = new Shooter();
    private final Limelight limelight = new Limelight();
    private final Turret turret = new Turret();
>>>>>>> 6b38d0c76c86587d631781dc85afae55046915ad

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
        shooter.setDefaultCommand(new SpinShooter(shooter, 0));
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {
<<<<<<< HEAD
        pilot.getAButtonObj().whenPressed(new FollowTrajectory(drivetrain));
        pilot.getBButtonObj().whenPressed(new SpinRotations(wheel));
        pilot.getXButtonObj().whenPressed(new SpinColor(wheel, Color.BLUE));
=======

        copilot.getAButtonObj().toggleWhenPressed(new SpinShooterFormula(shooter, limelight));

        copilot.getBButtonObj().whileHeld(new RunMagazine(magazine, 0.9));
        copilot.getBButtonObj().whenReleased(new RunMagazine(magazine, 0));
        copilot.getYButtonObj().whileHeld(new RunMagazine(magazine, -0.9));
        copilot.getYButtonObj().whenReleased(new RunMagazine(magazine, 0));

        copilot.getLbButtonObj().toggleWhenPressed(new AutoMagazine(magazine, limelight, shooter));

        pilot.getRbButtonObj().whileHeld(new DriveCenterPort(
                drivetrain, limelight, () -> pilot.getY(Hand.kLeft)
        ));

        pilot.getStartButtonObj().whenPressed(new KillAll());
        copilot.getStartButtonObj().whenPressed(new KillAll());

        copilot.getDpadLeftButtonObj().whileHeld(new ManualTurret(turret, 0.5));
        copilot.getDpadLeftButtonObj().whenReleased(new ManualTurret(turret, 0));
        copilot.getDpadRightButtonObj().whileHeld(new ManualTurret(turret, -0.5));
        copilot.getDpadRightButtonObj().whenReleased(new ManualTurret(turret, 0));
        pilot.getLbButtonObj().whileHeld(new SpinShooter(shooter, 2250));

        copilot.getXButtonObj().toggleWhenPressed(new AutoAim(turret, limelight));

        pilot.getAButtonObj().whenPressed(new LimelightAsCamera(limelight));

        copilot.getDpadUpButtonObj().whenPressed(new ZeroTurret(turret));

>>>>>>> 6b38d0c76c86587d631781dc85afae55046915ad
    }

}
