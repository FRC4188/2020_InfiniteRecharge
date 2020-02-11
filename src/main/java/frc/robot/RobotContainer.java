package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.commands.groups.LeftEnemyTrenchAuto;
import frc.robot.commands.groups.RightTrenchAuto;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.CspController;
import frc.robot.utils.CspSequentialCommandGroup;

/**
 * Class containing setup for robot.
 */
public class RobotContainer {

    // subsystem initialization
    private final Drivetrain drivetrain = new Drivetrain();

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
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {
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
        initialPose = autoChooser.getSelected().getInitialPose();
        return autoChooser.getSelected();
    }

}
