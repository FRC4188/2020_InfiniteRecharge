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

    public static final class MTrenchSixBall {
        public static final Pose2d INITIAL_POSE = new Pose2d(0, 0.0, new Rotation2d());

        public static final Waypoints DOWN_TRENCH = new Waypoints(
            List.of(
                INITIAL_POSE,
                //new Pose2d(1.2, 1.2, Rotation2d.fromDegrees(32)),
                new Pose2d(2.0, 1.57, new Rotation2d()),
                new Pose2d(2.98, 1.57, new Rotation2d()),
                new Pose2d(4.1, 1.58, new Rotation2d())
            ), false);

        public static final Waypoints TO_SHOOT = new Waypoints(
            List.of(
                new Pose2d(4.1, 1.58, new Rotation2d()),
                INITIAL_POSE
            ), true);
    }

    public static final class LTrenchSixBall {
        public static final Pose2d INITIAL_POSE = new Pose2d(0, 0, new Rotation2d());

        public static final Waypoints DOWN_TRECNH = new Waypoints(
            List.of(
                INITIAL_POSE,
                new Pose2d(2.0, 0.0, new Rotation2d()),
                new Pose2d(3.0, 0.0, new Rotation2d()),
                new Pose2d(4.2, 0.0, new Rotation2d())
            ), false);

        public static final Waypoints TO_SHOOT = new Waypoints(
            List.of(
                new Pose2d(4.2, 0.0, new Rotation2d()),
                new Pose2d(1.0, 0.0, new Rotation2d())
            ), true);
    }
    public static final class TrenchEightBall {
        public static final Pose2d INITIAL_POSE = new Pose2d(0, 0, new Rotation2d());

        public static final Waypoints DOWN_TRENCH = new Waypoints(
            List.of(
                INITIAL_POSE,
                new Pose2d(1.33, 1.32, Rotation2d.fromDegrees(37.5)),
                new Pose2d(2.12, 1.60, new Rotation2d()),
                new Pose2d(2.98, 1.60, new Rotation2d()),
                new Pose2d(3.9, 1.60, new Rotation2d())
            ), false);

        public static final Waypoints END_RENDEVOUS = new Waypoints(
            List.of(
                new Pose2d(4, 1.68, new Rotation2d()),
                new Pose2d(4.58, 0.19, Rotation2d.fromDegrees(-82.7)),
                new Pose2d(4.26, -1, Rotation2d.fromDegrees(-141.39)),
                new Pose2d(3.2, -1.84, Rotation2d.fromDegrees(-156.3))
            ), false);
        
        public static final Waypoints TO_SHOOT = new Waypoints(
            List.of(
                new Pose2d(3.38, -1.64, Rotation2d.fromDegrees(-156.3)),
                new Pose2d(0.25, -1.06, Rotation2d.fromDegrees(130.93))
            ), false);
    }

    public static final class WheelEightBall {
        public static final Pose2d INIT_POSE = new Pose2d(3.809158, 1.073795, new Rotation2d());

        public static final Waypoints TO_WHEEL = new Waypoints(
            List.of(
                INIT_POSE,
                new Pose2d(6.208496, 1.05592, new Rotation2d())
            ), false);
        
        public static final Waypoints TURN = new Waypoints(
            List.of(
                new Pose2d(6.208496, 1.05592, new Rotation2d()),
                new Pose2d(5.495295, 0.955895, Rotation2d.fromDegrees(18.1899))
            ), true);
        
        public static final Waypoints TO_FIRST_SHOT = new Waypoints(
            List.of(
                new Pose2d(5.495295, 0.955895, Rotation2d.fromDegrees(18.1899)),
                new Pose2d(6.00664, 1.87469, Rotation2d.fromDegrees(81.401677)),
                new Pose2d(5.535674, 3.598883, Rotation2d.fromDegrees(127.566386))
            ), false);
        
        public static final Waypoints TURN_IN = new Waypoints(
            List.of(
                new Pose2d(5.535674, 3.598883, Rotation2d.fromDegrees(127.566386)),
                new Pose2d(5.707125, 4.18986, Rotation2d.fromDegrees(22.63967))
            ), false);
        
        public static final Waypoints THROUGH_TO_SHOOT = new Waypoints(
            List.of(
                new Pose2d(5.707125, 4.18986, Rotation2d.fromDegrees(22.63967)),
                new Pose2d(7.135809, 4.998421, Rotation2d.fromDegrees(22.107565)),
                new Pose2d(7.651583, 5.788355, Rotation2d.fromDegrees(113.378157)),
                new Pose2d(5.73352, 5.862665, Rotation2d.fromDegrees(-169.963017))
            ), false);
    }
}
