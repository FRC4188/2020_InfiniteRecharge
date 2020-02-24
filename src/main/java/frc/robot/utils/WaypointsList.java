package frc.robot.utils;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import java.util.List;

/**
 * Class containing lists of Waypoints objects to be generated as trajectories.
 */
public class WaypointsList {

    /**
     * Drives straight one meter.
     */
    public static final Waypoints STRAIGHT = new Waypoints(
            List.of(
                new Pose2d(),
                new Pose2d(1, 0, new Rotation2d())
            ), false
    );

    /**
     * Drives an S-curve, ending 3 meters ahead of the starting point.
     */
    public static final Waypoints S_CURVE = new Waypoints(
            List.of(
                new Pose2d(0, 0, new Rotation2d()),
                new Pose2d(1, 1, new Rotation2d()),
                new Pose2d(2, -1, new Rotation2d()),
                new Pose2d(3, 0, new Rotation2d())
            ), false
    );

    /**
     * Starts right of center from bay, drives backward to trench.
     * Coordinates are field centric.
     */
    public static final Waypoints RIGHT_TO_BACK_TRENCH = new Waypoints(
            List.of(
                new Pose2d(3.45, -1.5, new Rotation2d()),
                new Pose2d(5, -0.75, new Rotation2d()),
                new Pose2d(7.55, -0.75, new Rotation2d())
            ), false
    );

    /**
     * Starts from back of trench and drives to front.
     */
    public static final Waypoints BACK_TO_FRONT_TRENCH = new Waypoints(
            List.of(
                new Pose2d(7.55, -0.75, new Rotation2d()),
                new Pose2d(5, -0.75, new Rotation2d())
            ), true
    );

    /**
     * Starts from front of trench and drives to bar.
     */
    public static final Waypoints FRONT_TRENCH_TO_BAR = new Waypoints(
            List.of(
                new Pose2d(5, -0.75, new Rotation2d()),
                new Pose2d(5.65, -2.5, Rotation2d.fromDegrees(-67.5))
            ), false
    );

    /**
     * Starts from left of field (facing power port) and drives
     * to power cells in enemy trench.
     */
    public static final Waypoints LEFT_TO_ENEMY_TRENCH = new Waypoints(
            List.of(
                new Pose2d(3.45, -7.5, new Rotation2d()),
                new Pose2d(5.85, -7.5, new Rotation2d())
            ), false
    );

    /**
     * Starts from enemy trench and drives to shooting position.
     */
    public static final Waypoints ENEMY_TRENCH_TO_SHOOT = new Waypoints(
            List.of(
                new Pose2d(5.85, -7.5, new Rotation2d()),
                new Pose2d(4.85, -6, Rotation2d.fromDegrees(-90)),
                new Pose2d(4.85, -4.5, Rotation2d.fromDegrees(-90))
            ), true
    );

    public static final Waypoints MID_DRIVE_AWAY = new Waypoints(
            List.of(
                new Pose2d(3.45, -3.0, new Rotation2d()),
                new Pose2d(4.55, -3.0, new Rotation2d())
            ), false
    );

    public static final Waypoints MID_DRIVE_TOWARD = new Waypoints(
            List.of(
                new Pose2d(3.45, -3.0, new Rotation2d()),
                new Pose2d(2.35, -3.0, new Rotation2d())
            ), false
    );

}
