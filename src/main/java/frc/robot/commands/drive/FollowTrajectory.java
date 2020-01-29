package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Drivetrain;

import java.io.IOException;
import java.util.List;

/**
 * Follows a trajectory using a RAMSETE controller.
 */
public class FollowTrajectory extends RamseteCommand {

    private final Drivetrain drivetrain;

    /**
     * Constructs a new FollowTrajectory command to drive a specified path given by
     * a list of Pose2d waypoints.
     *
     * @param drivetrain - Drivetrain subsystem to require.
     * @param waypoints - Poses to generate trajectory from.
     */
    public FollowTrajectory(Drivetrain drivetrain, List<Pose2d> waypoints) {
        super(TrajectoryGenerator.generateTrajectory(waypoints, drivetrain.getTrajectoryConfig()),
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
        this.drivetrain = drivetrain;
    }

    /**
     * Constructs a new FollowTrajectory command to drive a specified path given by
     * the name of a json path file.
     *
     * @param drivetrain - Drivetrain subsystem to require.
     * @param trajectoryJson - Name of the trajectory file in the deploy/paths directory
     * (e.g. "YourPath.wpilib.json").
     * @throws IOException
     */
    public FollowTrajectory(Drivetrain drivetrain, String trajectoryJson) throws IOException {
        super(TrajectoryUtil.fromPathweaverJson(
                Filesystem.getDeployDirectory().toPath().resolve("paths/" + trajectoryJson)),
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
        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.tankVolts(0, 0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
