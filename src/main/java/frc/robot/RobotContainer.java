package frc.robot;

import java.io.IOException;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.EmergencyPower;
import frc.robot.commands.climb.FireBrake;
import frc.robot.commands.climb.ManualClimb;
import frc.robot.commands.drive.DriveCenterPort;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.auto.EightBall;
import frc.robot.commands.skillschallenges.SkillsBounce;
import frc.robot.commands.skillschallenges.GalacticSearch;
import frc.robot.commands.skillschallenges.SkillsBarrel;
import frc.robot.commands.skillschallenges.SkillsSlolam;
import frc.robot.commands.skillschallenges.skillsaccuracy.AutoVelocities;
import frc.robot.commands.skillschallenges.skillsaccuracy.ReintroRoutine;
import frc.robot.commands.skillschallenges.skillsaccuracy.ShootingRoutine;
import frc.robot.commands.skillschallenges.skillsaccuracy.SkillsAutoFire;
import frc.robot.commands.auto.TrenchEightBall;
import frc.robot.commands.auto.TrenchSixBall;
import frc.robot.commands.auto.WheelEightBall;
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
import frc.robot.commands.turret.TurretToAngle;
import frc.robot.commands.turret.ZeroTurret;
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
import frc.robot.utils.Waypoints;
import frc.robot.utils.BrownoutProtection;
import frc.robot.utils.WaypointsList;

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
    //private final IntakeCamera camera = new IntakeCamera();
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
                () -> drivetrain.arcade(pilot.getY(Hand.kLeft, CspController.Scaling.CUBED), pilot.getX(Hand.kRight, CspController.Scaling.SQUARED), pilot.getBumper(Hand.kLeft)),
                drivetrain));

        turret.setDefaultCommand(new RunCommand(() -> turret.set(0.0), turret));
        //shooter.setDefaultCommand(new RunCommand(() -> shooter.setVelocity(2000), shooter));
        //magazine.setDefaultCommand(new RunCommand(() -> magazine.setVelocity(5000), magazine));
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {

        //pilot.getRbButtonObj().whileHeld(new DriveCenterPort(drivetrain, limelight, () -> pilot.getY(Hand.kLeft)));

        pilot.getDpadDownButtonObj().whenPressed(new LowerIntake(intake));
        pilot.getDpadUpButtonObj().whenPressed(new RaiseIntake(intake));

        pilot.getLtButtonObj().whileActiveContinuous(new AutoVelocities(shooter, limelight, turret, hood, true));
        pilot.getLtButtonObj().whenInactive(new AutoVelocities(shooter, limelight, turret, hood, false));

        //pilot.getYButtonObj().whileHeld(new RunMagazine(magazine, 1.0));
        pilot.getYButtonObj().whileHeld(new SkillsAutoFire(shooter, magazine, intake, limelight, turret, hood, true));
        pilot.getYButtonObj().whenReleased(new SkillsAutoFire(shooter, magazine, intake, limelight, turret, hood, false));

        pilot.getXButtonObj().whileHeld(new RunMagazine(magazine, -1.0));

        pilot.getBButtonObj().whileHeld(new RunCommand(() -> intake.spin(-1.0, -0.75), intake));
        pilot.getBButtonObj().whenReleased(new RunCommand(() -> intake.spin(0.0, 0.0), intake));

        pilot.getAButtonObj().whileHeld(new RunCommand(() -> intake.spin(0.0, 0.75), intake));
        pilot.getAButtonObj().whenReleased(new RunCommand(() -> intake.spin(0.0, 0.0), intake));


        //skills challenges
        //pilot.getYButtonObj().whileHeld(new SkillsAutoFire(shooter, magazine, intake, limelight, turret, hood, true));
        //pilot.getYButtonObj().whenReleased(new SkillsAutoFire(shooter, magazine, intake, limelight, turret, hood, false));

        //pilot.getRbButtonObj().whenPressed(new ReintroRoutine(drivetrain, intake, magazine));

        //pilot.getLbButtonObj().whenPressed(new ShootingRoutine(drivetrain));

        //pilot.getXButtonObj().whileHeld(new RunMagazine(magazine, -0.9));
        //pilot.getXButtonObj().whenReleased(new RunMagazine(magazine, 0));

        //pilot.getBButtonObj().whenPressed(new AutoMagazine(magazine, intake, true, true));
        //pilot.getBButtonObj().whenReleased(new AutoMagazine(magazine, intake, true, false));

        //pilot.getAButtonObj().whileHeld(new AutoMagazine(magazine, intake, false, true));
        //pilot.getAButtonObj().whenReleased(new AutoMagazine(magazine, intake, false, false));

        /*pilot.getBackButtonObj()
                .whenPressed(new EmergencyPower(drivetrain, shooter, turret, magazine, intake, wheelSpinner, true));
        pilot.getBackButtonObj()
                .whenReleased(new EmergencyPower(drivetrain, shooter, turret, magazine, intake, wheelSpinner, false));
*/
        pilot.getStartButtonObj().whenPressed(new KillAll());

        //skills challenges bindings
        //copilot.getXButtonObj().whenPressed(new BlueRoutine(drivetrain, intake, magazine, limelight, shooter, turret));
        //copilot.getYButtonObj().whenPressed(new YellowRoutine(drivetrain, intake, magazine, limelight, shooter, turret));
        //copilot.getBButtonObj().whenPressed(new RedRoutine(drivetrain, intake, magazine, limelight, shooter, turret));
        //copilot.getAButtonObj().whenPressed(new GreenRoutine(intake, magazine, shooter, hood, limelight, turret));
        //copilot.getRbButtonObj().whenPressed(new ReintroRoutine(drivetrain, intake, magazine));

        copilot.getStartButtonObj().whenPressed(new KillAll());

        //copilot.getAButtonObj().toggleWhenPressed(new FireBrake(climber));

        copilot.getBButtonObj().toggleWhenPressed(new ToggleHood(hood));

        copilot.getXButtonObj().whenPressed(new AutoAim(turret, limelight, 0, true));
        copilot.getXButtonObj().whenReleased(new AutoAim(turret, limelight, 0, false));


        copilot.getYButtonObj().toggleWhenPressed(new ToggleIntake(intake));

        /*copilot.getRtButtonObj().whileActiveContinuous(new AutoMagazine(magazine, intake, true, true));
        copilot.getRtButtonObj().whenInactive(new AutoMagazine(magazine, intake, true, false));
        copilot.getLtButtonObj().whileActiveContinuous(new AutoMagazine(magazine, intake, false, true));
        copilot.getLtButtonObj().whenInactive(new AutoMagazine(magazine, intake, false, false));*/

        copilot.getRtButtonObj().whenActive(new RunCommand(() -> intake.spin(-1.0, -0.75), intake));
        copilot.getRtButtonObj().whenInactive(new RunCommand(() -> intake.spin(0.0, 0.0), intake));

        copilot.getLtButtonObj().whenActive(new RunCommand(() -> intake.spin(1.0, 0.75), intake));
        copilot.getLtButtonObj().whenInactive(new RunCommand(() -> intake.spin(0.0, 0.0), intake));

        copilot.getDpadLeftButtonObj().whileHeld(new ManualTurret(turret, 0.2));
        copilot.getDpadLeftButtonObj().whenReleased(new ManualTurret(turret, 0));
        copilot.getDpadRightButtonObj().whileHeld(new ManualTurret(turret, -0.2));
        copilot.getDpadRightButtonObj().whenReleased(new ManualTurret(turret, 0));

        /*copilot.getRbButtonObj().whileHeld(new ManualClimb(climber, -0.9));
        copilot.getRbButtonObj().whenReleased(new ManualClimb(climber, 0));
        copilot.getLbButtonObj().whileHeld(new ManualClimb(climber, 0.6));
        copilot.getLbButtonObj().whenReleased(new ManualClimb(climber, 0));*/

       /* copilot.getRbButtonObj().whileHeld(new RunCommand(() -> intake.spin(-1.0, -0.75), intake));
        copilot.getRbButtonObj().whenReleased(new RunCommand(() -> intake.spin(0.0, 0.0), intake));

        copilot.getLbButtonObj().whileHeld(new RunCommand(() -> intake.spin(1.0, 0.75), intake));
        copilot.getLbButtonObj().whenReleased(new RunCommand(() -> intake.spin(0.0, 0.0), intake));

        copilot.getBackButtonObj().whenPressed(new ZeroTurret(turret));*/

        buttonBox.getButton1Obj().whenPressed(new TurretAngle(turret, 0));
        buttonBox.getButton3Obj().whenPressed(new TurretAngle(turret, 180));
        buttonBox.getButton2Obj().whenPressed(new SpinShooter(shooter, 3000));
        buttonBox.getButton4Obj().toggleWhenPressed(new SpinShooter(shooter, 2000));
        buttonBox.getButton5Obj().toggleWhenPressed(new SpinShooter(shooter, 3900));
        buttonBox.getButton6Obj().toggleWhenPressed(new SpinShooter(shooter, 3100));
        buttonBox.getButton7Obj().toggleWhenPressed(new SpinShooter(shooter, 3300));

        SmartDashboard.putData("Set T Angle", new InstantCommand(() -> turret.setAngle(SmartDashboard.getNumber("Set Turret Angle", 0.0)), turret));
        SmartDashboard.putData("Set S Velocity", new InstantCommand(() -> shooter.setVelocity(SmartDashboard.getNumber("Set Shooter Velocity", 0.0)), shooter));
        SmartDashboard.putData("Set M Voltage", new InstantCommand(() -> magazine.set(SmartDashboard.getNumber("Set Magazine Voltage", 0.0)), magazine));
        SmartDashboard.putData("Set M Velocity", new InstantCommand(() -> magazine.setVelocity(SmartDashboard.getNumber("Set Magazine Velocity", 0.0)), magazine));
        SmartDashboard.putData("Set M PIDs", new InstantCommand(() -> magazine.setPIDs(SmartDashboard.getNumber("Set Magazine P", 0.0), SmartDashboard.getNumber("Set Magazine D", 0.0)), magazine));
    }

    /**
     * Configures and places auto chooser on dashboard.
     * 
     * @throws IOException
     */
    private void putChooser() throws IOException {
        autoChooser.addOption("Do Nothing", null);
        
        autoChooser.addOption("Left Trench 6-Ball", new TrenchSixBall(drivetrain, turret, shooter, magazine, intake, limelight)
        );
        autoChooser.addOption("Left Trench 8-Ball (A)", new TrenchEightBall(drivetrain, turret, shooter, magazine, intake, limelight)
        );
        autoChooser.addOption("Left Trench 8-Ball (B)", new EightBall(drivetrain, shooter, turret, magazine, intake, limelight)
        );
        autoChooser.addOption("Wheel 8-Ball", new WheelEightBall(drivetrain, shooter, turret, limelight, intake, magazine)
        );

        autoChooser.addOption("Skills Barrel" , new SkillsBarrel(drivetrain));
        autoChooser.addOption("Skills Bounce", new SkillsBounce(drivetrain));
        autoChooser.addOption("Skills Slolam", new SkillsSlolam(drivetrain));
        //autoChooser.addOption("Galatic Search", new GalacticSearch(drivetrain, intake, limelight));
        
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
