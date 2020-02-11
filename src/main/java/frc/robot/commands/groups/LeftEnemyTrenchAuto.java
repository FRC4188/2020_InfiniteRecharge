package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.CspSequentialCommandGroup;
import frc.robot.utils.WaypointsList;

/**
 * Class containing command group to run Left Enemy Trench auto.
 */
public class LeftEnemyTrenchAuto extends CspSequentialCommandGroup {

    /**
     * Constructs RightTrenchAuto object and adds commands to group.
     */
    public LeftEnemyTrenchAuto(Drivetrain drivetrain) {
        addCommands(
                new FollowTrajectory(drivetrain, WaypointsList.LEFT_TO_ENEMY_TRENCH),
                new FollowTrajectory(drivetrain, WaypointsList.ENEMY_TRENCH_TO_SHOOT)
        );
    }

    /**
     * Returns initial pose required for this command group.
     */
    public Pose2d getInitialPose() {
        return WaypointsList.LEFT_TO_ENEMY_TRENCH.getPoses().get(0);
    }

}
