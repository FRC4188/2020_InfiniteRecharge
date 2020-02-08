package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.CspController;
import frc.robot.utils.Waypoints;

/**
 * Class containing setup for robot.
 */
public class RobotContainer {

    // subsystem initialization
    private final Drivetrain drivetrain = new Drivetrain();

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
        drivetrain.reset(Waypoints.LEFT_TO_ENEMY_TRENCH.get(0));
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
        pilot.getAButtonObj().whenPressed(new FollowTrajectory(drivetrain, Waypoints.LEFT_TO_ENEMY_TRENCH));
        pilot.getYButtonObj().whenPressed(new FollowTrajectory(drivetrain, Waypoints.ENEMY_TRENCH_TO_SHOOT, true));

    }

}
