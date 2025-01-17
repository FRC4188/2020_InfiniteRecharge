package frc.robot;

import java.io.IOException;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.EmergencyPower;
import frc.robot.commands.climb.AutoClimb;
import frc.robot.commands.climb.FireBrake;
import frc.robot.commands.climb.ManualClimb;
import frc.robot.commands.drive.CenterBall;
import frc.robot.commands.drive.DriveCenterPort;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.auto.LTrenchSixBall;
import frc.robot.commands.auto.MTrenchSixBall;
import frc.robot.commands.auto.TrenchEightBall;
import frc.robot.commands.auto.WheelEightBall;
import frc.robot.commands.auto.WheelFiveBall;
import frc.robot.commands.hood.ToggleHood;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.intake.SpinIndexer;
import frc.robot.commands.intake.SpinJustIntake;
import frc.robot.commands.intake.ToggleIntake;
import frc.robot.commands.magazine.AutoMagazine;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.AutoFire;
import frc.robot.commands.shooter.AutoFireQuantity;
import frc.robot.commands.shooter.InMotionFire;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.test.MotorTest;
import frc.robot.commands.turret.AutoAim;
import frc.robot.commands.turret.ManualTurret;
import frc.robot.commands.turret.TurretToAngle;
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
import frc.robot.utils.Waypoints;
import frc.robot.utils.BrownoutProtection;
import frc.robot.utils.WaypointsList;
import frc.robot.utils.CspController.Scaling;

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
    private final TempManager tempManager = new TempManager(climber, drivetrain, intake, magazine, shooter, turret);
    private final BrownoutProtection bop = new BrownoutProtection(drivetrain, intake, magazine, shooter, turret);

    // controller initialization
    private final CspController pilot = new CspController(0);
    private final CspController copilot = new CspController(1);
    private final ButtonBox buttonBox = new ButtonBox(2);

    // EMERGENCY POWER!!!!!!

    // auto chooser initialization
    private final SendableChooser<CspSequentialCommandGroup> autoChooser = new SendableChooser<CspSequentialCommandGroup>();

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
     * 
     * @throws IOException
     */
    public RobotContainer() throws IOException {
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
                () -> drivetrain.arcade(pilot.getY(Hand.kLeft), pilot.getX(Hand.kRight, Scaling.CUBED), pilot.getBumper(Hand.kLeft)),
                drivetrain));
        turret.setDefaultCommand(new RunCommand(() -> turret.set(0.0), turret));;
    
        shooter.setDefaultCommand(new SpinShooter(shooter, 2500.0));
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {
        pilot.getRbButtonObj().whileHeld(new CenterBall(drivetrain, camera, () -> pilot.getY(Hand.kLeft)));
        pilot.getLbButtonObj().whenPressed(new ToggleIntake(intake));

        pilot.getDpadDownButtonObj().whenPressed(new LowerIntake(intake));
        pilot.getDpadUpButtonObj().whenPressed(new RaiseIntake(intake));

        pilot.getYButtonObj().whileHeld(new AutoFire(drivetrain, shooter, magazine, intake, limelight, turret, true));
        pilot.getYButtonObj().whenReleased(new AutoFire(drivetrain, shooter, magazine, intake, limelight, turret, false));

       // pilot.getYButtonObj().whileHeld(new InMotionFire(drivetrain, shooter, turret, limelight));
       // pilot.getYButtonObj().whenReleased(new InMotionFire(drivetrain, shooter, turret, limelight));

        pilot.getXButtonObj().whileHeld(new RunMagazine(magazine, -0.9));
        pilot.getXButtonObj().whenReleased(new RunMagazine(magazine, 0));

        pilot.getBButtonObj().whileHeld(new AutoMagazine(magazine, intake, true, true));
        pilot.getBButtonObj().whenReleased(new AutoMagazine(magazine, intake, true, false));

        pilot.getAButtonObj().whileHeld(new AutoMagazine(magazine, intake, false, true));
        pilot.getAButtonObj().whenReleased(new AutoMagazine(magazine, intake, false, false));

        
        /*pilot.getRbButtonObj().whileHeld(new ManualClimb(climber, -0.9));
        pilot.getRbButtonObj().whenReleased(new ManualClimb(climber, 0));

        pilot.getLbButtonObj().whileHeld(new RunCommand(() -> climber.set(0.4), climber));
        pilot.getLbButtonObj().whenReleased(new RunCommand(() -> climber.set(-0.0), climber));*/

        pilot.getBackButtonObj()
                .whenPressed(new EmergencyPower(drivetrain, shooter, turret, magazine, intake, wheelSpinner, true));
        pilot.getBackButtonObj()
                .whenReleased(new EmergencyPower(drivetrain, shooter, turret, magazine, intake, wheelSpinner, false));

        pilot.getStartButtonObj().whenPressed(new KillAll());

        copilot.getStartButtonObj().whenPressed(new KillAll());

        copilot.getAButtonObj().whenPressed(new InstantCommand(() -> climber.engagePneuBrake(true), climber));
        copilot.getAButtonObj().whenReleased(new InstantCommand(() -> climber.engagePneuBrake(false), climber));

        copilot.getBButtonObj().toggleWhenPressed(new ToggleHood(hood));

        copilot.getXButtonObj().whileHeld(new RunMagazine(magazine, 1.0));
        copilot.getXButtonObj().whenReleased(new RunMagazine(magazine, 0.0));

        copilot.getYButtonObj().whenPressed(new FireBrake(climber));

        copilot.getRtButtonObj().whileActiveContinuous(new AutoMagazine(magazine, intake, true, true));
        copilot.getLtButtonObj().whileActiveContinuous(new AutoMagazine(magazine, intake, false, true));

        copilot.getDpadLeftButtonObj().whileHeld(new ManualTurret(turret, 0.2));
        copilot.getDpadLeftButtonObj().whenReleased(new ManualTurret(turret, 0));
        copilot.getDpadRightButtonObj().whileHeld(new ManualTurret(turret, -0.2));
        copilot.getDpadRightButtonObj().whenReleased(new ManualTurret(turret, 0));

        copilot.getRbButtonObj().whileHeld(new ManualClimb(climber, -0.9));
        copilot.getRbButtonObj().whenReleased(new ManualClimb(climber, 0));
        copilot.getLbButtonObj().whileHeld(new ManualClimb(climber, 0.6));
        copilot.getLbButtonObj().whenReleased(new ManualClimb(climber, 0));

        copilot.getBackButtonObj().whenPressed(new ZeroTurret(turret));

        buttonBox.getButton2Obj().whenPressed(new SpinJustIntake(intake, -1.0))
            .whenReleased(new SpinJustIntake(intake, 0.0));
        buttonBox.getButton1Obj().whenPressed(new SpinIndexer(intake, -0.75))
            .whenReleased(new SpinIndexer(intake, 0.0));
        buttonBox.getButton3Obj().whenPressed(new SpinIndexer(intake, 0.75))
            .whenReleased(new SpinIndexer(intake, 0.0));
        buttonBox.getButton4Obj().whenPressed(new RunMagazine(magazine, 0.625))
            .whenReleased(new RunMagazine(magazine, 0.0));
        buttonBox.getButton5Obj().whenPressed(new SpinShooter(shooter, 3500.0))
            .whenReleased(new SpinShooter(shooter, 2000.0));
        buttonBox.getButton6Obj().whileHeld(new TurretToAngle(turret, 0.0));
        buttonBox.getButton7Obj().whenPressed(new TurretToAngle(turret, 180.0));
        buttonBox.getButton8Obj().whileHeld(new RunCommand(() -> {
            intake.spin(0.0, 0.0);
            magazine.set(0.0);
            climber.set(0.0);
            turret.set(0.0);
            shooter.set(0.0);
        }, intake, magazine, climber, turret, shooter));

        SmartDashboard.putData("Set Shooter PIDs", new RunCommand(() -> shooter.setPIDs(
            SmartDashboard.getNumber("Set S P", 0.0),
            SmartDashboard.getNumber("Set S I", 0.0),
            SmartDashboard.getNumber("Set S D", 0.0)), shooter));
        SmartDashboard.putData("Set Shooter Velocity", new InstantCommand(() -> shooter.setVelocity(SmartDashboard.getNumber("Set S Velocity", 0.0)), shooter));

        SmartDashboard.putData("Set Turret Angle PIDs", new RunCommand(() -> turret.setPIDs(
            SmartDashboard.getNumber("Set T Angle P", 0.0),
            SmartDashboard.getNumber("Set T Angle I", 0.0),
            SmartDashboard.getNumber("Set T Angle D", 0.0)), turret));
        SmartDashboard.putData("Set Turret Angle", new TurretToAngle(turret, SmartDashboard.getNumber("Set T Angle", 0.0)));

        SmartDashboard.putData("Rezero Turret", new ZeroTurret(turret));

        SmartDashboard.putData("Rezero Odometry", new InstantCommand(() -> drivetrain.reset(initialPose), drivetrain));
    }

    /**
     * Configures and places auto chooser on dashboard.
     * 
     * @throws IOException
     */
    private void putChooser() throws IOException {
        autoChooser.addOption("Do Nothing", null);
        
        autoChooser.addOption("Left Trench 6-Ball (M)", new MTrenchSixBall(drivetrain, turret, shooter, magazine, intake, limelight));
        autoChooser.addOption("Left Trench 6-Ball (L)", new LTrenchSixBall(drivetrain, shooter, intake, magazine, limelight, turret));
        //autoChooser.addOption("Left Trench 8-Ball", new TrenchEightBall(drivetrain, turret, shooter, magazine, intake, limelight));
        autoChooser.addOption("Wheel 5-Ball", new WheelFiveBall(drivetrain, magazine, intake, shooter, limelight, turret));
        
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

    public Command getTestCommand() {
        return new MotorTest(shooter, intake, magazine, turret);
    }
}
