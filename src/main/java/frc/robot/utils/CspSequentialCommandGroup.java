package frc.robot.utils;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * Class representing a SequentialCommandGroup with the added functionality of storing
 * information about initial pose.
 */
public class CspSequentialCommandGroup extends SequentialCommandGroup {

    /**
     * Returns initial pose of command group. Intended to be overridden by subclasses.
     */
    public Pose2d getInitialPose() {
        return new Pose2d();
    }

}
