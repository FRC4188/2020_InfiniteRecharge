package frc.robot.commands.vision;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;

/**
 * Sets limelight to track target.
 */
public class CameraTrack extends CommandBase {

    private final Limelight limelight;

    /**
     * Constructs a new CameraTrack command to set limelight to track target.
     *
     * @param limelight - Limelight subsystem to use.
     */
    public CameraTrack(Limelight limelight) {
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
