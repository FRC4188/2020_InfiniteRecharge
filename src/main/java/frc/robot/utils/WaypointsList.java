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

    public static class SkillsBarrel{
        public static final Pose2d INIT_POSE = new Pose2d(1.231766, 2.158743, new Rotation2d());

        public static final Waypoints FIRST_PATH = new Waypoints(
            List.of(
                INIT_POSE,
                new Pose2d(4.154927, 2.47283, Rotation2d.fromDegrees(-28.164715)),
                new Pose2d(4.24235, 0.895475, Rotation2d.fromDegrees(-142.94124)),
                new Pose2d(3.217062, 0.978769, Rotation2d.fromDegrees(128.752911)),
                new Pose2d(3.330627, 2.073289, Rotation2d.fromDegrees(46.588199)),
                new Pose2d(4.337374, 2.396301, Rotation2d.fromDegrees(-9.317688)),
                new Pose2d(6.116931, 2.279222, Rotation2d.fromDegrees(19.459996)),
                new Pose2d(6.743719, 3.664895, Rotation2d.fromDegrees(147.266417)),
                new Pose2d(5.416644, 2.963658, Rotation2d.fromDegrees(-86.415925)),
                new Pose2d(7.45, 0.9, Rotation2d.fromDegrees(5.271789)),
                new Pose2d(8.6, 1.566251, Rotation2d.fromDegrees(85.318856)),
                new Pose2d(7.673829, 2.3, Rotation2d.fromDegrees(170.871806)),
                new Pose2d(4.0, 2.6, new Rotation2d(Math.PI)),
                new Pose2d(1.3, 2.6, new Rotation2d(Math.PI))
            ), false);
    }
    public static class SkillsBounce {
        public static final Pose2d INIT_POSE = new Pose2d(1.239757, 2.528515, new Rotation2d());

        public static final Waypoints TO_A3 = new Waypoints(
            List.of(
                INIT_POSE,
                new Pose2d(2.070697, 2.623568, Rotation2d.fromDegrees(32.719334)),
                new Pose2d(2.4, 3.4, new Rotation2d(Math.PI / 2.0))
            ), false);
        
        public static final Waypoints TO_A6 = new Waypoints(
            List.of(
                new Pose2d(2.41, 3.433746, new Rotation2d(Math.PI / 2.0)),
                new Pose2d(3.6, 1.050225, Rotation2d.fromDegrees(122.50758)),
                new Pose2d(4.255460, 1.076126, Rotation2d.fromDegrees(-136.163005)),
                new Pose2d(4.7, 3.433746, new Rotation2d(-Math.PI / 2.0))
            ), true);
        
        public static final Waypoints TO_A9 = new Waypoints(
            List.of(
                new Pose2d(4.584796, 3.433746, new Rotation2d(-Math.PI / 2.0)),
                new Pose2d(5.6, 1.0, Rotation2d.fromDegrees(-32.9277)),
                new Pose2d(6.9, 1.0, Rotation2d.fromDegrees(80)),
                new Pose2d(7.2, 3.2, Rotation2d.fromDegrees(120.0))
            ), false);
        
        public static final Waypoints TO_FINISH = new Waypoints(
            List.of(
                new Pose2d(7.74, 2.96, Rotation2d.fromDegrees(120)),
                new Pose2d(8.504493, 2.1, Rotation2d.fromDegrees(180))
            ), true);
    }

    public static final class Slolam {
        public static final Pose2d INIT_POSE = new Pose2d(1.235629, 0.819711, new Rotation2d());

        public static final Waypoints PATH = new Waypoints(
            List.of(
                INIT_POSE,
                new Pose2d(2.324021, 1.552842, Rotation2d.fromDegrees(71.578044)),
                new Pose2d(3.895293, 2.2, Rotation2d.fromDegrees(6.566273)),
                new Pose2d(7, 1.8, Rotation2d.fromDegrees(-30)),
                new Pose2d(7.75, 0.86, Rotation2d.fromDegrees(12.20)),
               // new Pose2d(8.1, 0.999095, Rotation2d.fromDegrees(43.303656)),
                new Pose2d(8.3, 2.253535, Rotation2d.fromDegrees(136.801569)),   
                new Pose2d(7.6, 2.231251, Rotation2d.fromDegrees(-114.139601)),
                new Pose2d(6.7, 1.011095, Rotation2d.fromDegrees(-142.198426)),
                new Pose2d(5.522480, 0.676122, Rotation2d.fromDegrees(-179.998429)),
                new Pose2d(3.8, 0.676122, Rotation2d.fromDegrees(173.6)),
                new Pose2d(2.8, 1.411052, Rotation2d.fromDegrees(125.26)),
                new Pose2d(1, 2, new Rotation2d(Math.PI))
                //new Pose2d(4.361523, 0.702955, Rotation2d.fromDegrees(177.668344)),
                //new Pose2d(3.3, 0.702955, Rotation2d.fromDegrees(156.655507)),
                //new Pose2d(2.123487, 1.982748, Rotation2d.fromDegrees(140.401563)),
                //new Pose2d(1.124212, 2.316583, Rotation2d.fromDegrees(176.613329))
            ), false);
    }

    public static final class SkillsAccuracy {
        public static final Pose2d INIT_POSE = new Pose2d(0, 0, new Rotation2d()); //point in green zone against the wall
        public static final Pose2d TO_REINTRO = new Pose2d(-6.83, 0.0, new Rotation2d());
        public static final Pose2d TO_YELLOW = new Pose2d(-2.63, 0, new Rotation2d());
        public static final Pose2d TO_BLUE = new Pose2d(-3.96, 0.27, new Rotation2d());
        public static final Pose2d TO_RED = new Pose2d(-5.39, 0.45, new Rotation2d());

    }
}
