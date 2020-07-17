package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.Waypoints;
import java.io.IOException;

/**
 * Follows a trajectory using a RAMSETE controller.
 */
public class FollowTrajectory extends RamseteCommand {

    private final Drivetrain drivetrain;

    /**
     * Constructs a new FollowTrajectory command to drive a specified trajectory.
     *
     * @param drivetrain - Drivetrain subsystem to require.
     * @param trajectory - Trajectory to drive.
     */
    public FollowTrajectory(Drivetrain drivetrain, Trajectory trajectory) {
        super(trajectory,
                drivetrain::getPose,
                new RamseteController(),
                drivetrain.getFeedforward(),
                drivetrain.getKinematics(),
                drivetrain::getWheelSpeeds,
                drivetrain.getLeftPid(),
                drivetrain.getRightPid(),
                drivetrain::tankVolts,
                drivetrain
        );
        addRequirements(drivetrain);
        this.drivetrain = drivetrain;
    }

    /**
     * Constructs a new FollowTrajectory command to drive a specified path given by
     * a Waypoints object.
     *
     * @param drivetrain - Drivetrain subsystem to require.
     * @param waypoints - Waypoints to generate trajectory from.
     */
    public FollowTrajectory(Drivetrain drivetrain, Waypoints waypoints) {
        this(drivetrain, TrajectoryGenerator.generateTrajectory(
                waypoints.getPoses(),
                drivetrain.getTrajectoryConfig().setReversed(waypoints.isReversed())
        ));
    }

    /**
     * Constructs a new FollowTrajectory command to drive a specified path given by
     * the name of a json path file.
     *
     * @param drivetrain - Drivetrain subsystem to require.
     * @param trajectoryJson - Name of the trajectory file in the deploy/paths directory
     *      (e.g. "YourPath.wpilib.json").
     * @throws IOException - Thrown if file cannot be resolved.
     */
    public FollowTrajectory(Drivetrain drivetrain, String trajectoryJson) throws IOException {
        this(drivetrain, TrajectoryUtil.fromPathweaverJson(
                Filesystem.getDeployDirectory().toPath().resolve("paths/" + trajectoryJson))
        );
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        drivetrain.tankVolts(0, 0);
    }

    @Override
    public boolean isFinished() {
        return super.isFinished();
    }

}