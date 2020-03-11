package frc.robot.commands.vision;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;

/**
 * Sets limelight to track target.
 */
public class CameraCloseTrack extends CommandBase {

    private final Limelight limelight;

    /**
     * Constructs a new CameraCloseTrack command to set limelight
     * to track target at close distances.
     *
     * @param limelight - Limelight subsystem to use.
     */
    public CameraCloseTrack(Limelight limelight) {
        this.limelight = limelight;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        limelight.trackTarget();
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
