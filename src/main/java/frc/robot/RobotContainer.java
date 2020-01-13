package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.drive.ManualDrive;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.CspController;

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
     * Sets the default command for each subsystem, if applicable.
     */
    private void setDefaultCommands() {
        drivetrain.setDefaultCommand(new ManualDrive(drivetrain,
                () -> pilot.getY(Hand.kLeft),
                () -> pilot.getX(Hand.kRight)));
    }

    /**
     * Binds commands to buttons on controllers.
     */
    private void configureButtonBindings() {
        pilot.getAButtonObj().whenPressed(new FollowTrajectory(drivetrain));
    }

}
