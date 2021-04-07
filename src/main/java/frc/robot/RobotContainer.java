package frc.robot;

import java.io.IOException;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.auto.EightBall;
import frc.robot.commands.skillschallenges.skillsaccuracy.aiden.BallTrack;
import frc.robot.commands.auto.TrenchEightBall;
import frc.robot.commands.auto.TrenchSixBall;
import frc.robot.commands.auto.WheelEightBall;
import frc.robot.commands.hood.ToggleHood;
import frc.robot.commands.intake.LowerIntake;
import frc.robot.commands.intake.RaiseIntake;
import frc.robot.commands.intake.SpinJustIntake;
import frc.robot.commands.intake.ToggleIntake;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.AutoAim;
import frc.robot.commands.turret.ManualTurret;
import frc.robot.commands.turret.TurretAngle;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
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
    //private final IntakeCamera camera = new IntakeCamera();
    private final Intake intake = new Intake();
    private final Hood hood = new Hood();
    private final Limelight limelight = new Limelight();
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
                () -> drivetrain.arcade(pilot.getY(Hand.kLeft, CspController.Scaling.LINEAR), pilot.getX(Hand.kRight, CspController.Scaling.SQUARED), pilot.getBumper(Hand.kRight)),
                drivetrain));

        turret.setDefaultCommand(new RunCommand(() -> turret.set(0.0), turret));
        shooter.setDefaultCommand(new RunCommand(() -> shooter.setVelocity(3100), shooter));
        magazine.setDefaultCommand(new BallTrack(intake, magazine, shooter, limelight,
        (() -> copilot.getYButton()), (() -> pilot.getBButton()), (() -> pilot.getYButton()), () -> pilot.getBumper(Hand.kLeft)));
       }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {

        //pilot.getRbButtonObj().whileHeld(new DriveCenterPort(drivetrain, limelight, () -> pilot.getY(Hand.kLeft)));

        pilot.getDpadDownButtonObj().whenPressed(new LowerIntake(intake));
        pilot.getDpadUpButtonObj().whenPressed(new RaiseIntake(intake));

        //pilot.getLtButtonObj().whileActiveContinuous(new AutoVelocities(shooter, limelight, turret, hood, true));
        //pilot.getLtButtonObj().whenInactive(new AutoVelocities(shooter, limelight, turret, hood, false));

        pilot.getLtButtonObj().whileActiveContinuous(new AutoAim(turret, limelight, -1.3, true));
        pilot.getLtButtonObj().whenInactive(new AutoAim(turret, limelight, -1.3, false));

        pilot.getStartButtonObj().whenPressed(new KillAll());

        copilot.getStartButtonObj().whenPressed(new KillAll());

        copilot.getBButtonObj().toggleWhenPressed(new ToggleHood(hood));

        copilot.getXButtonObj().whenPressed(new AutoAim(turret, limelight, 0, true));
        copilot.getXButtonObj().whenReleased(new AutoAim(turret, limelight, 0, false));

        //copilot.getYButtonObj().toggleWhenPressed(new ToggleIntake(intake));

        //copilot.getYButtonObj().whileHeld(new RunCommand(() -> intake.spinIntake(-0.75), intake));
        //copilot.getYButtonObj().whenReleased(new RunCommand(() -> intake.spinIntake(0.0), intake));

        copilot.getDpadLeftButtonObj().whileHeld(new ManualTurret(turret, 0.2));
        copilot.getDpadLeftButtonObj().whenReleased(new ManualTurret(turret, 0));
        copilot.getDpadRightButtonObj().whileHeld(new ManualTurret(turret, -0.2));
        copilot.getDpadRightButtonObj().whenReleased(new ManualTurret(turret, 0));

        buttonBox.getButton1Obj().whenPressed(new TurretAngle(turret, 0.0));
        buttonBox.getButton3Obj().whenPressed(new TurretAngle(turret, 180.0));
        buttonBox.getButton2Obj().whenPressed(new SpinShooter(shooter, 3100.0));
        buttonBox.getButton4Obj().toggleWhenPressed(new SpinShooter(shooter, 2000.0));
        buttonBox.getButton5Obj().toggleWhenPressed(new SpinShooter(shooter, 0.0));

        SmartDashboard.putData("Set T Angle", new InstantCommand(() -> turret.setAngle(SmartDashboard.getNumber("Set Turret Angle", 0.0)), turret));
        SmartDashboard.putData("Set S Velocity", new RunCommand(() -> shooter.setVelocity(SmartDashboard.getNumber("Set Shooter Velocity", 0.0)), shooter));
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

        /*autoChooser.addOption("Skills Barrel" , new SkillsBarrel(drivetrain));
        autoChooser.addOption("Skills Bounce", new SkillsBounce(drivetrain));
        autoChooser.addOption("Skills Slolam", new SkillsSlolam(drivetrain));
        
        SmartDashboard.putData("Auto Chooser", autoChooser);*/
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
