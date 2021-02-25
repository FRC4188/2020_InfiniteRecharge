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
                new Pose2d(6.1, -2.7, Rotation2d.fromDegrees(-60))
            ), false
    );

    /**
     * Starts from bar and drives to shooting position.
     */
    public static final Waypoints BAR_TO_SHOOT = new Waypoints(
            List.of(
                new Pose2d(6.1, -2.7, Rotation2d.fromDegrees(-60)),
                new Pose2d(5.5, -2.3, Rotation2d.fromDegrees(0))
            ), true
    );

    /**
     * Starts from left of field (facing power port) and drives
     * to power cells in enemy trench.
     */
    public static final Waypoints LEFT_TO_ENEMY_TRENCH = new Waypoints(
            List.of(
                new Pose2d(3.45, -7.5, new Rotation2d()),
                new Pose2d(6.125, -7.5, new Rotation2d())
            ), false
    );

    /**
     * Starts from enemy trench and drives to shooting position.
     */
    public static final Waypoints ENEMY_TRENCH_TO_SHOOT = new Waypoints(
            List.of(
                new Pose2d(6.125, -7.5, new Rotation2d()),
                new Pose2d(3.8, -3.0, Rotation2d.fromDegrees(-60))
            ), true
    );

    /**
     * Starts from enemy trench and drives to shooting position.
     */
    public static final Waypoints SHOOT_TO_BAR = new Waypoints(
            List.of(
                new Pose2d(3.8, -3.0, Rotation2d.fromDegrees(-60)),
                new Pose2d(5.25, -4.3, Rotation2d.fromDegrees(30))
            ), false
    );

    /**
     * Starts from directly in front of the port and drives away in a straight line.
     */
    public static final Waypoints MID_DRIVE_AWAY = new Waypoints(
            List.of(
                new Pose2d(3.45, -2.45, new Rotation2d()),
                new Pose2d(4.55, -2.45, new Rotation2d())
            ), false
    );

    /**
     * Starts from directly in front of the port and drives forward in a straight line.
     */
    public static final Waypoints MID_DRIVE_TOWARD = new Waypoints(
            List.of(
                new Pose2d(3.45, -2.45, new Rotation2d()),
                new Pose2d(2.35, -2.45, new Rotation2d())
            ), true
    );

    public static final class TrenchSixBall {
        private static final double[] trenchStart = {2.579, 1.672};
        private static final double[] trenchEnd = {8, trenchStart[1]};
        private static final double[] shooting = {2.889, 0.395};

        public static final Waypoints DOWN_TRENCH = new Waypoints(
            List.of(
                new Pose2d(),
                new Pose2d(trenchStart[0], trenchStart[1], new Rotation2d()),
                new Pose2d(trenchEnd[0], trenchEnd[1], new Rotation2d())
            ), false);

        public static final Waypoints TO_SHOOT = new Waypoints(
            List.of(
                new Pose2d(trenchEnd[0], trenchEnd[1], new Rotation2d()),
                new Pose2d(shooting[0], shooting[1], new Rotation2d(-1.118, -0.083))
            ), true);
    }

    public static final class TrenchEightBall {
        public static final Pose2d INITIAL_POSE = new Pose2d(3.664413, 5.663597, new Rotation2d());

        public static final Waypoints DOWN_TRENCH = new Waypoints(
            List.of(
                INITIAL_POSE,
                new Pose2d(6.4, 7.17481, new Rotation2d()),
                new Pose2d(8.0, 7.1839, new Rotation2d())
            ), false);

        public static final Waypoints INTO_RENDEVOUS = new Waypoints(
            List.of(
                new Pose2d(7.85735, 6.262254, Rotation2d.fromDegrees(109.990414)),
                new Pose2d(8.903125, 5.005867, Rotation2d.fromDegrees(-155.662961))
            ), true);

        public static final Waypoints THROUGH_RENDEVOUS = new Waypoints(
            List.of(
                new Pose2d(8.903125, 5.005867, Rotation2d.fromDegrees(-155.662961)),
                new Pose2d(6.515633, 4.023848, Rotation2d.fromDegrees(-155.662961))
            ), false);
        
        public static final Waypoints TO_SHOOT = new Waypoints(
            List.of(
                new Pose2d(6.677321, 4.163156, Rotation2d.fromDegrees(-155.662961)),
                new Pose2d(5.26001, 4.191111, Rotation2d.fromDegrees(147.24539))
            ), false);
    }

}
