package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.climb.FireBrake;
import frc.robot.commands.climb.ManualClimb;
import frc.robot.commands.drive.DriveCenterPort;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.commands.groups.LeftEnemyTrenchAuto;
import frc.robot.commands.groups.MidDriveAwayAuto;
import frc.robot.commands.groups.MidDriveTowardAuto;
import frc.robot.commands.groups.RightTrenchAuto;
import frc.robot.commands.hood.ToggleHood;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intake.SpinJustIntake;
import frc.robot.commands.intake.ToggleIntake;
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.shooter.SpinShooterFormula;
import frc.robot.commands.turret.AutoAim;
import frc.robot.commands.turret.ManualTurret;
import frc.robot.commands.turret.TurretAngle;
import frc.robot.commands.turret.ZeroTurret;
import frc.robot.commands.vision.CameraCloseTrack;
import frc.robot.commands.vision.CameraZoomTrack;
import frc.robot.commands.vision.UseAsCamera;
import frc.robot.commands.wheel.LowerWheel;
import frc.robot.commands.wheel.RaiseWheel;
import frc.robot.commands.wheel.SpinWheel;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.WheelSpinner;
import frc.robot.utils.ButtonBox;
import frc.robot.utils.CspController;
import frc.robot.utils.CspSequentialCommandGroup;
import frc.robot.utils.KillAll;
import frc.robot.utils.TempManager;

/**
 * Class containing setup for robot.
 */
public class RobotContainer {

    // subsystem initialization
    private final Drivetrain drivetrain = new Drivetrain();
    private final Magazine magazine = new Magazine();
    private final Shooter shooter = new Shooter();
    private final Turret turret = new Turret();
    private final Climber climber = new Climber();
    private final Intake intake = new Intake();
    private final Hood hood = new Hood();
    private final Limelight limelight = new Limelight();
    private final WheelSpinner wheelSpinner = new WheelSpinner();
    private final TempManager tempManager =
            new TempManager(climber, drivetrain, intake, magazine, shooter, turret);

    // controller initialization
    private final CspController pilot = new CspController(0);
    private final CspController copilot = new CspController(1);
    private final ButtonBox buttonBox = new ButtonBox(2);

    // auto chooser initialization
    private final SendableChooser<CspSequentialCommandGroup> autoChooser =
            new SendableChooser<CspSequentialCommandGroup>();

    // state variables
    private Pose2d initialPose = new Pose2d();

    public TempManager getTempManager() {
        return tempManager;
    }

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
                () -> pilot.getX(Hand.kRight),
                () -> pilot.getBumper(Hand.kLeft)
        ));
        shooter.setDefaultCommand(new SpinShooter(shooter, 3000));
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {

        pilot.getRbButtonObj().whileHeld(new DriveCenterPort(
                drivetrain, limelight, () -> pilot.getY(Hand.kLeft)
        ));

        pilot.getDpadRightButtonObj().whenPressed(new UseAsCamera(limelight));
        pilot.getDpadLeftButtonObj().whenPressed(new CameraCloseTrack(limelight));
        pilot.getLbButtonObj().whenPressed(new CameraZoomTrack(limelight));

        pilot.getLtButtonObj().whileActiveContinuous(new SpinWheel(wheelSpinner, -0.7));
        pilot.getLtButtonObj().whenInactive(new SpinWheel(wheelSpinner, 0));
        pilot.getRtButtonObj().whileActiveContinuous(new SpinWheel(wheelSpinner, 0.7));
        pilot.getRtButtonObj().whenInactive(new SpinWheel(wheelSpinner, 0));

        pilot.getDpadDownButtonObj().whenPressed(new LowerWheel(wheelSpinner));
        pilot.getDpadUpButtonObj().whenPressed(new RaiseWheel(wheelSpinner));

        pilot.getXButtonObj().whileHeld(new RunMagazine(magazine, -0.9));
        pilot.getXButtonObj().whenReleased(new RunMagazine(magazine, 0));
        pilot.getYButtonObj().whileHeld(new RunMagazine(magazine, 0.9));
        pilot.getYButtonObj().whenReleased(new RunMagazine(magazine, 0));

        pilot.getBButtonObj().whileHeld(new SpinIntake(intake, 1.0));
        pilot.getBButtonObj().whenReleased(new SpinIntake(intake, 0));
        pilot.getAButtonObj().whileHeld(new SpinIntake(intake, -.85));
        pilot.getAButtonObj().whenReleased(new SpinIntake(intake, 0));

        pilot.getBackButtonObj().toggleWhenPressed(
                new AutoMagazine(magazine, intake, limelight, shooter)
        );

        pilot.getStartButtonObj().whenPressed(new KillAll());
        copilot.getStartButtonObj().whenPressed(new KillAll());

        copilot.getBackButtonObj().toggleWhenPressed(new FireBrake(climber));

        copilot.getYButtonObj().whenPressed(new ZeroTurret(turret));

        copilot.getBButtonObj().toggleWhenPressed(new ToggleHood(hood));

        copilot.getAButtonObj().whenPressed(new ToggleIntake(intake));

        copilot.getXButtonObj().toggleWhenPressed(new AutoAim(turret, limelight, 0));

        copilot.getDpadLeftButtonObj().whileHeld(new ManualTurret(turret, 0.3));
        copilot.getDpadLeftButtonObj().whenReleased(new ManualTurret(turret, 0));
        copilot.getDpadRightButtonObj().whileHeld(new ManualTurret(turret, -0.3));
        copilot.getDpadRightButtonObj().whenReleased(new ManualTurret(turret, 0));


        copilot.getRbButtonObj().whileHeld(new ManualClimb(climber, -0.9));
        copilot.getRbButtonObj().whenReleased(new ManualClimb(climber, 0));
        copilot.getLbButtonObj().whileHeld(new ManualClimb(climber, 0.6));
        copilot.getLbButtonObj().whenReleased(new ManualClimb(climber, 0));

        copilot.getDpadUpButtonObj().whileHeld(new SpinJustIntake(intake, -1));
        copilot.getDpadUpButtonObj().whenReleased(new SpinJustIntake(intake, 0));

        buttonBox.getButton1Obj().whenPressed(new TurretAngle(turret, 0));
        buttonBox.getButton2Obj().toggleWhenPressed(new SpinShooterFormula(shooter, limelight));
        buttonBox.getButton3Obj().toggleWhenPressed(new SpinShooter(shooter, 3600));
        buttonBox.getButton4Obj().whenPressed(new TurretAngle(turret, 180));
        buttonBox.getButton5Obj().toggleWhenPressed(new SpinShooter(shooter, 4550));
        buttonBox.getButton6Obj().toggleWhenPressed(new SpinShooter(shooter, 6000));
        buttonBox.getButton7Obj().toggleWhenPressed(new SpinShooter(shooter, 2250));
        buttonBox.getButton8Obj().whileHeld(new SpinJustIntake(intake, -1.0));
        buttonBox.getButton8Obj().whenReleased(new SpinJustIntake(intake, 0));
        buttonBox.getButton9Obj().whenPressed(new EmergencyPower(pilot, true));
        buttonBox.getButton9Obj().whenReleased(new EmergencyPower(pilot, false));

    }

    /**
     * Configures and places auto chooser on dashboard.
     */
    private void putChooser() {
        autoChooser.addOption("Do Nothing", null);
        autoChooser.addOption("Right Trench", new RightTrenchAuto(drivetrain, magazine, shooter,
                limelight, turret, intake
        ));
        autoChooser.addOption("Left Enemy Trench", new LeftEnemyTrenchAuto(drivetrain, magazine,
                shooter, limelight, turret, intake
        ));
        autoChooser.addOption("Mid Drive Away", new MidDriveAwayAuto(drivetrain, magazine, shooter,
                limelight, turret
        ));
        autoChooser.addOption("Mid Drive Toward", new MidDriveTowardAuto(drivetrain, magazine,
                shooter, limelight, turret
        ));
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
