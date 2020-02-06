package frc.robot.utils;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import java.util.List;

/**
 * Class containing lists of Pose2d objects to be generated as trajectories.
 */
public class Waypoints {

    /**
     * Drives straight one meter.
     */
    public static final List<Pose2d> STRAIGHT = List.of(
            new Pose2d(),
            new Pose2d(1, 0, new Rotation2d())
    );

    /**
     * Drives an S-curve, ending 3 meters ahead of the starting point.
     */
    public static final List<Pose2d> S_CURVE = List.of(
            new Pose2d(0, 0, new Rotation2d()),
            new Pose2d(1, 1, new Rotation2d()),
            new Pose2d(2, -1, new Rotation2d()),
            new Pose2d(3, 0, new Rotation2d())
    );

    /**
     * Starts right of center from bay, drives backward to trench.
     * Coordinates are field centric.
     */
    public static final List<Pose2d> RIGHT_TO_TRENCH = List.of(
            new Pose2d(3, -1.5, new Rotation2d()),
            new Pose2d(5, -0.75, new Rotation2d()),
            new Pose2d(8, -0.75, new Rotation2d())
    );

}