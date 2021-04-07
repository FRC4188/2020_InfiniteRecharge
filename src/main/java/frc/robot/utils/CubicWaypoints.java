package frc.robot.utils;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;

import java.util.List;

/**
 * Class representing list of Pose2d objects and associated information for
 * trajectory generation.
 */
public class CubicWaypoints {

    private final Pose2d start;
    private final Pose2d end;
    private final List<Translation2d> poses;
    private final boolean isReversed;

    /**
     * Constructs a Waypoints object.
     * @param poses - poses to store in object.
     * @param isReversed - whether or not the poses are intended to be followed in reverse.
     */
    public CubicWaypoints(Pose2d start, List<Translation2d> poses, Pose2d end, boolean isReversed) {
        this.start = start;
        this.end = end;
        this.poses = poses;
        this.isReversed = isReversed;
    }

    public Pose2d getStart() {
        return start;
    }

    public Pose2d getEnd() {
        return end;
    }
    
	/**
     * Returns the list of Pose2d objects associated with the object.
     */
    public List<Translation2d> getPoses() {
        return poses;
    }

    /**
     * Returns whether or not the poses are intended to be followed in reverse.
     */
    public boolean isReversed() {
        return isReversed;
    }

}
