package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.drive.DriveCenterPort;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.commands.groups.LeftEnemyTrenchAuto;
import frc.robot.commands.groups.RightTrenchAuto;
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
import frc.robot.utils.CspController;
import frc.robot.utils.CspSequentialCommandGroup;
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

    // auto chooser initialization
    private final SendableChooser<CspSequentialCommandGroup> autoChooser =
            new SendableChooser<CspSequentialCommandGroup>();

    // state variables
    private Pose2d initialPose = new Pose2d();

    /**
     * Initializes robot subsystems, controllers, commands, and chooser.
     */
    public RobotContainer() {
        setDefaultCommands();
        configureButtonBindings();
        putChooser();
    }

    /**
     * Resets variables and sensors for each subsystem.
     */
    public void resetSubsystems() {
        drivetrain.reset(initialPose);
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

    }

    /**
     * Configures and places auto chooser on dashboard.
     */
    private void putChooser() {
        autoChooser.addOption("Do Nothing", null);
        autoChooser.addOption("Right Trench", new RightTrenchAuto(drivetrain));
        autoChooser.addOption("Left Enemy Trench", new LeftEnemyTrenchAuto(drivetrain));
        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    /**
     * Returns the currently selected command from the auto chooser and gets its initial pose.
     */
    public Command getAutoCommand() {
        Command autoCommand = autoChooser.getSelected();
        if (autoCommand != null) {
            initialPose = autoChooser.getSelected().getInitialPose();
        } else {
            initialPose = new Pose2d();
        }
        return autoCommand;
    }

}
