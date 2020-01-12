package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Drivetrain;
import java.util.List;

/**
 * Manually controls drivetrain using pilot controller.
 */
public class FollowTrajectory extends RamseteCommand {

    private final Drivetrain drivetrain;

    private static final double kS = 0; // volts
    private static final double kV = 0; // volt seconds / meter
    private static final double kA = 0; // volt seconds squared / meter
    private static final double kP = 0;
    private static final double MAX_VELOCITY = 3; // meters / second
    private static final double MAX_ACCELERATION = 3; // meters / second squared
    private static final double MAX_VOLTAGE = 10; // volts
    private static final double TRACKWIDTH = 0; // meters

    private static DifferentialDriveKinematics driveKinematics =
            new DifferentialDriveKinematics(TRACKWIDTH);

    private static DifferentialDriveVoltageConstraint voltageConstraint =
            new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(kS, kV, kA),
            driveKinematics, MAX_VOLTAGE);

    private static TrajectoryConfig config =
            new TrajectoryConfig(MAX_VELOCITY, MAX_ACCELERATION)
            .setKinematics(driveKinematics)
            .addConstraint(voltageConstraint);

    private static Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(
                    new Translation2d(1, 1),
                    new Translation2d(2, -1)
            ),
            new Pose2d(3, 0, new Rotation2d(0)),
            config
    );

    public FollowTrajectory(Drivetrain drivetrain) {
        super(trajectory,
                drivetrain::getPose,
                new RamseteController(),
                new SimpleMotorFeedforward(kS, kV, kA),
                driveKinematics,
                drivetrain::getWheelSpeeds,
                new PIDController(kP, 0, 0),
                new PIDController(kP, 0, 0),
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
