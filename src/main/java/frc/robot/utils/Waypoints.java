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
    public static final List<Pose2d> RIGHT_TO_BACK_TRENCH = List.of(
            new Pose2d(3.45, -1.5, new Rotation2d()),
            new Pose2d(5, -0.75, new Rotation2d()),
            new Pose2d(7.55, -0.75, new Rotation2d())
    );

    /**
     * Starts from back of trench and drives to front.
     */
    public static final List<Pose2d> BACK_TO_FRONT_TRENCH = List.of(
            new Pose2d(7.55, -0.75, new Rotation2d()),
            new Pose2d(5, -0.75, new Rotation2d())
    );

    /**
     * Starts from front of trench and drives to bar.
     */
    public static final List<Pose2d> FRONT_TRENCH_TO_BAR = List.of(
            new Pose2d(5, -0.75, new Rotation2d()),
            new Pose2d(5.65, -2.5, Rotation2d.fromDegrees(-67.5))
    );

    /**
     * Starts from left of field (facing power port) and drives
     * to power cells in enemy trench.
     */
    public static final List<Pose2d> LEFT_TO_ENEMY_TRENCH = List.of(
            new Pose2d(3.45, -7.5, new Rotation2d()),
            new Pose2d(5.85, -7.5, new Rotation2d())
    );

    /**
     * Starts from enemy trench and drives to shooting position.
     */
    public static final List<Pose2d> ENEMY_TRENCH_TO_SHOOT = List.of(
            new Pose2d(5.85, -7.5, new Rotation2d()),
            new Pose2d(4.85, -6, Rotation2d.fromDegrees(-90)),
            new Pose2d(4.85, -3.5, Rotation2d.fromDegrees(-90))
    );

}