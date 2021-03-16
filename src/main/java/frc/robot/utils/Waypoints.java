package frc.robot.utils;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import java.util.List;

/**
 * Class representing list of Pose2d objects and associated information for
 * trajectory generation.
 */
public class Waypoints {

    private final List<Pose2d> poses;
    private final boolean isReversed;

    /**
     * Constructs a Waypoints object.
     * @param poses - poses to store in object.
     * @param isReversed - whether or not the poses are intended to be followed in reverse.
     */
    public Waypoints(List<Pose2d> poses, boolean isReversed) {
        this.poses = poses;
        this.isReversed = isReversed;
    }
    
	/**
     * Returns the list of Pose2d objects associated with the object.
     */
    public List<Pose2d> getPoses() {
        return poses;
    }

    /**
     * Returns whether or not the poses are intended to be followed in reverse.
     */
    public boolean isReversed() {
        return isReversed;
    }

}
