package frc.robot.utils.paths;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import java.util.List;

/**
 * Class containing lists of Waypoints objects to be generated as trajectories.
 */
public class WaypointsList {

    public static final class Test {
        public static final Pose2d INITIAL_POSE = new Pose2d();

        public static final Waypoints ONE_METER = new Waypoints(
            List.of(
                INITIAL_POSE,
                new Pose2d(15.0, 0.0, new Rotation2d())
            ), false);
        
        public static final Waypoints CURVE = new Waypoints(
            List.of(
                INITIAL_POSE,
                new Pose2d(1.0, 1.0, new Rotation2d(Math.PI / 2))
            ), false);
    }

    public static final class TrenchSixBall {
        public static final Pose2d INITIAL_POSE = new Pose2d(3.664413, 5.663597, new Rotation2d());

        public static final Waypoints DOWN_TRENCH = new Waypoints(
            List.of(
                INITIAL_POSE,
                new Pose2d(6.4, 7.17481, new Rotation2d()),
                new Pose2d(8.0, 7.1839, new Rotation2d())
            ), false);

        public static final Waypoints TO_SHOOT = new Waypoints(
            List.of(
                new Pose2d(8.0, 7.1839, new Rotation2d()),
                new Pose2d(5.343132, 5.858764, Rotation2d.fromDegrees(6.862765))
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
    public static class SkillsObstacle {
        public static final Pose2d INIT_POSE = new Pose2d(1.126210, 2.3, new Rotation2d());

        public static final Waypoints FIRSTMOVE = new Waypoints(
            List.of(
                INIT_POSE,
                new Pose2d(2.999564, 2.3, new Rotation2d()),
                new Pose2d(5, 1, Rotation2d.fromDegrees(-169.6133844)),
                new Pose2d(2.858936, 1.9, Rotation2d.fromDegrees(12.07)),
                new Pose2d(5.441378, 2.25, Rotation2d.fromDegrees(12.1)),
                new Pose2d(7, 2.7, Rotation2d.fromDegrees(103)),
                new Pose2d(6.446506, 3.6, Rotation2d.fromDegrees(172.900789)),
                new Pose2d(5.261222, 3.6, Rotation2d.fromDegrees(-72.888431)),
                new Pose2d(7.412093, 0.978, Rotation2d.fromDegrees(-4.421744)),
                new Pose2d(8.4, 1.7, Rotation2d.fromDegrees(89.284147)),
                new Pose2d(7.367684, 2.2789, Rotation2d.fromDegrees(176.213568)),
                new Pose2d(2.0, 2.7, Rotation2d.fromDegrees(175))
            ), false);

    }

}
