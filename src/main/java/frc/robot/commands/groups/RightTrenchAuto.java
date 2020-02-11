package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.CspSequentialCommandGroup;
import frc.robot.utils.WaypointsList;

/**
 * Class containing command group to run Right Trench auto.
 */
public class RightTrenchAuto extends CspSequentialCommandGroup {

    /**
     * Constructs RightTrenchAuto object and adds commands to group.
     */
    public RightTrenchAuto(Drivetrain drivetrain) {
        addCommands(
                new FollowTrajectory(drivetrain, WaypointsList.RIGHT_TO_BACK_TRENCH),
                new FollowTrajectory(drivetrain, WaypointsList.BACK_TO_FRONT_TRENCH),
                new FollowTrajectory(drivetrain, WaypointsList.FRONT_TRENCH_TO_BAR)
        );
    }

    /**
     * Returns initial pose required for this command group.
     */
    public Pose2d getInitialPose() {
        return WaypointsList.RIGHT_TO_BACK_TRENCH.getPoses().get(0);
    }

}
