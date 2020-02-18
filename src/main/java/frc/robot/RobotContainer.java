package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import frc.robot.commands.climber.ManualClimb;
import frc.robot.commands.drive.DriveCenterPort;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.commands.groups.AutoShoot;
import frc.robot.commands.hood.HoodToggle;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.magazine.RunMagazine;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.shooter.SpinShooterFormula;
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
        turret.setDefaultCommand(new ConditionalCommand(new Spin360(turret, 0.5), 
                new AutoAim(turret, limelight), turret::getSpin));
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

        copilot.getLbButtonObj().toggleWhenPressed(new AutoShoot(magazine, limelight, shooter));

        copilot.getDpadLeftButtonObj().whileHeld(new ManualTurret(turret, 0.5));
        copilot.getDpadLeftButtonObj().whenReleased(new ManualTurret(turret, 0));
        copilot.getDpadRightButtonObj().whileHeld(new ManualTurret(turret, -0.5));
        copilot.getDpadRightButtonObj().whenReleased(new ManualTurret(turret, 0));

        copilot.getDpadUpButtonObj().whileHeld(new ManualClimb(.9, climber));
        copilot.getDpadUpButtonObj().whenReleased(new ManualClimb(0, climber));
        copilot.getDpadDownButtonObj().whileHeld(new ManualClimb(-.6, climber));
        copilot.getDpadDownButtonObj().whenReleased(new ManualClimb(0, climber));

        copilot.getStartButtonObj().whenPressed(new KillAll());
        pilot.getStartButtonObj().whenPressed(new KillAll());

        pilot.getRbButtonObj().whileHeld(new DriveCenterPort(
                drivetrain, limelight, () -> pilot.getY(Hand.kLeft)
        ));

        pilot.getLbButtonObj().toggleWhenPressed(new AutoShoot(magazine, limelight, shooter));

        pilot.getAButtonObj().whenPressed(new CameraOff(limelight));
        pilot.getBButtonObj().whenPressed(new CameraTrack(limelight));
        pilot.getYButtonObj().whenPressed(new CameraZoom(limelight));

        pilot.getDpadUpButtonObj().whenPressed(new ZeroTurret(turret));

        pilot.getDpadDownButtonObj().whenPressed(new HoodToggle(hood));

        pilot.getXButtonObj().whileHeld(new SpinIntake(intake, 0.5));
        pilot.getXButtonObj().whenReleased(new SpinIntake(intake, 0));

    }

}