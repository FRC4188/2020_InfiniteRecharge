package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Climber.ManualClimb;
import frc.robot.commands.drive.AutoCenterBay;
import frc.robot.commands.drive.CenterBay;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.commands.groups.CancelShoot;
import frc.robot.commands.groups.Shoot;
import frc.robot.commands.intake.FireIntake;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.magazine.AutoBelt;
import frc.robot.commands.magazine.TurnBelt;
import frc.robot.commands.shooter.CancelShooter;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.AutoTurret;
import frc.robot.commands.turret.ManualTurret;
import frc.robot.commands.turret.Spin360;
import frc.robot.commands.turret.TurnTurret;
import frc.robot.commands.hood.HoodToggle;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Hood;

import frc.robot.utils.CspController;
import frc.robot.utils.KillAll;

/**
 * Class containing setup for robot.
 */
public class RobotContainer {

    // subsystem initialization
    private final Drivetrain drivetrain = new Drivetrain();
    private final Magazine magazine = new Magazine();
    private final Climber climber = new Climber();
    private static final Shooter shooter = new Shooter();
    private static final Limelight limelight = new Limelight();
    private final Turret turret = new Turret();
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
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {
        copilot.getBButtonObj().whileHeld(new TurnBelt(magazine, 0.9));
        copilot.getBButtonObj().whenReleased(new TurnBelt(magazine, 0.0));
        copilot.getYButtonObj().whenHeld(new TurnBelt(magazine, -0.9));
        copilot.getYButtonObj().whenHeld(new TurnBelt(magazine, 0.0));

        copilot.getAButtonObj().toggleWhenPressed(new SpinShooter(shooter, limelight));

        copilot.getRsButtonObj().whileHeld(new Shoot(shooter, limelight, magazine));
        copilot.getRsButtonObj().whenReleased(new CancelShoot(shooter, limelight, magazine));

        copilot.getDpadUpButtonObj().whenPressed(new HoodToggle(hood));

        pilot.getRbButtonObj().whileHeld(new CenterBay(drivetrain, limelight, pilot.getY(Hand.kLeft)));

        pilot.getStartButtonObj().whenPressed(new KillAll());
        copilot.getStartButtonObj().whenPressed(new KillAll());

        copilot.getDpadLeftButtonObj().whileHeld(new ManualTurret(turret, -0.5));
        copilot.getDpadLeftButtonObj().whenReleased(new ManualTurret(turret, 0));
        copilot.getDpadRightButtonObj().whileHeld(new ManualTurret(turret, 0.5));
        copilot.getDpadRightButtonObj().whenReleased(new ManualTurret(turret, 0));

        copilot.getXButtonObj().toggleWhenPressed(new AutoTurret(limelight, turret));

        //pilot.getLbButtonObj().whenPressed(new Spin360(turret, 1));

        pilot.getDpadUpButtonObj().whileHeld(new ManualClimb(.9, climber));
        pilot.getDpadUpButtonObj().whenReleased(new ManualClimb(0, climber));
        pilot.getDpadDownButtonObj().whileHeld(new ManualClimb(-.6, climber));
        pilot.getDpadDownButtonObj().whenReleased(new ManualClimb(0, climber));

        copilot.getLbButtonObj().whileHeld(new SpinIntake(-1.0, intake));
        copilot.getLbButtonObj().whenReleased(new SpinIntake(0.0, intake));
        copilot.getRbButtonObj().whileHeld(new SpinIntake(1.0, intake));
        copilot.getRbButtonObj().whenReleased(new SpinIntake(0.0, intake));

        copilot.getLsButtonObj().whenPressed(new FireIntake(intake));
    }

    public static Limelight getLimelight(){
        return limelight;
    }

    public static Shooter getShooter(){
        return shooter;
    }
}