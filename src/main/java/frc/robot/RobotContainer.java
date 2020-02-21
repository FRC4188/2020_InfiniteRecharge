package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.climber.FireBrake;
import frc.robot.commands.climber.ManualClimb;
import frc.robot.commands.drive.DriveCenterPort;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.commands.groups.AutoShoot;
import frc.robot.commands.groups.LeftEnemyTrenchAuto;
import frc.robot.commands.groups.RightTrenchAuto;
import frc.robot.commands.hood.ToggleHood;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intake.ToggleIntake;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.AutoAim;
import frc.robot.commands.turret.ManualTurret;
import frc.robot.commands.turret.Spin360;
import frc.robot.commands.turret.ZeroTurret;
import frc.robot.commands.vision.CameraOff;
import frc.robot.commands.vision.CameraTrack;
import frc.robot.commands.vision.CameraZoom;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Intake;
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
    private final Climber climber = new Climber();
    private final Intake intake = new Intake();
    private final Hood hood = new Hood();

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
        turret.resetEncoders();
    }

    /**
     * Sets the default command for each subsystem, if applicable.
     */
    private void setDefaultCommands() {
        drivetrain.setDefaultCommand(new ManualDrive(drivetrain,
                () -> pilot.getY(Hand.kLeft),
                () -> -pilot.getX(Hand.kRight),
                () -> pilot.getBumper(Hand.kLeft)
        ));
        shooter.setDefaultCommand(new SpinShooter(shooter, 0));
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {

        pilot.getRbButtonObj().whileHeld(new DriveCenterPort(
                drivetrain, limelight, () -> pilot.getY(Hand.kLeft)
        ));

        pilot.getDpadRightButtonObj().whenPressed(new CameraOff(limelight));
        pilot.getDpadLeftButtonObj().whenPressed(new CameraTrack(limelight));
        pilot.getLbButtonObj().whenPressed(new CameraZoom(limelight));

        pilot.getYButtonObj().whileHeld(new RunMagazine(magazine, 0.9));
        pilot.getYButtonObj().whenReleased(new RunMagazine(magazine, 0));
        pilot.getXButtonObj().whileHeld(new RunMagazine(magazine, -0.9));
        pilot.getXButtonObj().whenReleased(new RunMagazine(magazine, 0));

        pilot.getDpadUpButtonObj().whenPressed(new ZeroTurret(turret));

        pilot.getDpadDownButtonObj().whenPressed(new ToggleHood(hood));

        pilot.getBButtonObj().whileHeld(new SpinIntake(intake, 1.0));
        pilot.getBButtonObj().whenReleased(new SpinIntake(intake, 0));
        pilot.getAButtonObj().whileHeld(new SpinIntake(intake, -.85));
        pilot.getAButtonObj().whenReleased(new SpinIntake(intake, 0));

        pilot.getStartButtonObj().whenPressed(new KillAll());
        copilot.getStartButtonObj().whenPressed(new KillAll());

        copilot.getRbButtonObj().whenPressed(new ToggleIntake(intake));

        copilot.getAButtonObj().toggleWhenPressed(new AutoAim(turret, limelight));

        copilot.getLbButtonObj().toggleWhenPressed(new AutoShoot(magazine, limelight, shooter));

        copilot.getDpadLeftButtonObj().whileHeld(new ManualTurret(turret, 0.3));
        copilot.getDpadLeftButtonObj().whenReleased(new ManualTurret(turret, 0));
        copilot.getDpadRightButtonObj().whileHeld(new ManualTurret(turret, -0.3));
        copilot.getDpadRightButtonObj().whenReleased(new ManualTurret(turret, 0));

        copilot.getDpadUpButtonObj().whileHeld(new ManualClimb(climber, -0.9));
        copilot.getDpadUpButtonObj().whenReleased(new ManualClimb(climber, 0));
        copilot.getDpadDownButtonObj().whileHeld(new ManualClimb(climber, 0.6));
        copilot.getDpadDownButtonObj().whenReleased(new ManualClimb(climber, 0));

        copilot.getBButtonObj().whenPressed(new Spin360(turret, limelight));

        copilot.getBackButtonObj().toggleWhenPressed(new FireBrake(climber));

    }

    /**
     * Configures and places auto chooser on dashboard.
     */
    private void putChooser() {
        autoChooser.addOption("Do Nothing", null);
        autoChooser.addOption("Right Trench", new RightTrenchAuto(drivetrain, magazine, shooter,
                limelight, turret
        ));
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