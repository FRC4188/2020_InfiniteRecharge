package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.EmergencyPower;
import frc.robot.commands.climb.FireBrake;
import frc.robot.commands.climb.ManualClimb;
import frc.robot.commands.drive.DriveCenterPort;
<<<<<<< HEAD
import frc.robot.commands.groups.TrenchEightBall;
import frc.robot.commands.groups.TrenchSixBall;
=======
import frc.robot.commands.drive.ManualDrive;
import frc.robot.commands.groups.old.LeftEnemyTrenchAuto;
import frc.robot.commands.groups.old.MidDriveAwayAuto;
import frc.robot.commands.groups.old.MidDriveTowardAuto;
import frc.robot.commands.groups.old.RightTrenchAuto;
//import frc.robot.commands.groups.AutoShoot;
>>>>>>> c6c325503255b2f26ba74d7047b5d14f90d6cf47
import frc.robot.commands.hood.ToggleHood;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.intake.ToggleIntake;
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.AutoFire;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.AutoAim;
import frc.robot.commands.turret.ManualTurret;
import frc.robot.commands.turret.TurretAngle;
import frc.robot.commands.turret.ZeroTurret;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeCamera;
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
import frc.robot.utils.BrownoutProtection;

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
    private final IntakeCamera camera = new IntakeCamera();
    private final Intake intake = new Intake();
    private final Hood hood = new Hood();
    private final Limelight limelight = new Limelight();
    private final WheelSpinner wheelSpinner = new WheelSpinner();
    private final TempManager tempManager =
            new TempManager(climber, drivetrain, intake, magazine, shooter, turret);
    private final BrownoutProtection bop = new BrownoutProtection(drivetrain, intake, magazine, shooter, turret);

    // controller initialization
    private final CspController pilot = new CspController(0);
    private final CspController copilot = new CspController(1);
    private final ButtonBox buttonBox = new ButtonBox(2);

    // EMERGENCY POWER!!!!!!

    // auto chooser initialization
    private final SendableChooser<CspSequentialCommandGroup> autoChooser =
            new SendableChooser<CspSequentialCommandGroup>();

    // state variables
    private Pose2d initialPose = new Pose2d();

    public TempManager getTempManager() {
        return tempManager;
    }

    public BrownoutProtection getBrownoutProtection() {
        return bop;
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
        drivetrain.setDefaultCommand(new RunCommand(
            () -> drivetrain.arcade(pilot.getY(Hand.kLeft), pilot.getX(Hand.kRight), pilot.getBumper(Hand.kLeft)), drivetrain
         ));

        shooter.setDefaultCommand(new SpinShooter(shooter, 3500.0));
    }
    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {

        pilot.getRbButtonObj().whileHeld(new DriveCenterPort(
                drivetrain, limelight, () -> pilot.getY(Hand.kLeft)
        )); 
             
        pilot.getDpadDownButtonObj().whenPressed(new LowerIntake(intake));
        pilot.getDpadUpButtonObj().whenPressed(new RaiseIntake(intake));

        pilot.getYButtonObj().whileHeld(new AutoFire(shooter, magazine, intake, limelight, turret));

        pilot.getXButtonObj().whileHeld(new RunMagazine(magazine, -0.9));
        pilot.getXButtonObj().whenReleased(new RunMagazine(magazine, 0));

        pilot.getBButtonObj().whileHeld(new AutoMagazine(magazine, intake, true, true));
        pilot.getBButtonObj().whileHeld(new AutoMagazine(magazine, intake, true, false));

        pilot.getAButtonObj().whileHeld(new AutoMagazine(magazine, intake, false, true));
        pilot.getAButtonObj().whenReleased(new AutoMagazine(magazine, intake, false, false));

        pilot.getBackButtonObj().whenPressed(new EmergencyPower(drivetrain, shooter, turret, magazine, intake, wheelSpinner, true));
        pilot.getBackButtonObj().whenReleased(new EmergencyPower(drivetrain, shooter, turret, magazine, intake, wheelSpinner, false));

        pilot.getStartButtonObj().whenPressed(new KillAll());
        copilot.getStartButtonObj().whenPressed(new KillAll());

        copilot.getAButtonObj().toggleWhenPressed(new FireBrake(climber));

        copilot.getBButtonObj().toggleWhenPressed(new ToggleHood(hood));

        copilot.getXButtonObj().toggleWhenPressed(new AutoAim(turret, limelight, 0));

        copilot.getYButtonObj().toggleWhenPressed(new ToggleIntake(intake));

        copilot.getRtButtonObj().whileActiveContinuous(new AutoMagazine(magazine, intake, true, true));
        copilot.getLtButtonObj().whileActiveContinuous(new AutoMagazine(magazine, intake, false, true));

        copilot.getDpadLeftButtonObj().whileHeld(new ManualTurret(turret, 0.1));
        copilot.getDpadLeftButtonObj().whenReleased(new ManualTurret(turret, 0));
        copilot.getDpadRightButtonObj().whileHeld(new ManualTurret(turret, -0.1));
        copilot.getDpadRightButtonObj().whenReleased(new ManualTurret(turret, 0));

        copilot.getRbButtonObj().whileHeld(new ManualClimb(climber, -0.9));
        copilot.getRbButtonObj().whenReleased(new ManualClimb(climber, 0));
        copilot.getLbButtonObj().whileHeld(new ManualClimb(climber, 0.6));
        copilot.getLbButtonObj().whenReleased(new ManualClimb(climber, 0));

        copilot.getBackButtonObj().whenPressed(new ZeroTurret(turret));

        buttonBox.getButton1Obj().whenPressed(new TurretAngle(turret, 0));
        buttonBox.getButton3Obj().toggleWhenPressed(new SpinShooter(shooter, 3600));
        buttonBox.getButton4Obj().whenPressed(new TurretAngle(turret, 180));
        buttonBox.getButton5Obj().toggleWhenPressed(new SpinShooter(shooter, 4550));
        buttonBox.getButton6Obj().toggleWhenPressed(new SpinShooter(shooter, 6000));
        buttonBox.getButton7Obj().toggleWhenPressed(new SpinShooter(shooter, 2250));
    }

    /**
     * Configures and places auto chooser on dashboard.
     */
    private void putChooser() {
        autoChooser.addOption("Do Nothing", null);
<<<<<<< HEAD
        
        autoChooser.addOption("Left Trench 6-Ball", new TrenchSixBall(drivetrain, turret, shooter, magazine, intake, limelight)
        );
        autoChooser.addOption("Left Trench 8-Ball", new TrenchEightBall(drivetrain, turret, shooter, magazine, intake, limelight)
        );
=======
>>>>>>> c6c325503255b2f26ba74d7047b5d14f90d6cf47

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
