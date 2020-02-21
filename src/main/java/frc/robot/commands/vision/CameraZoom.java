package frc.robot.commands.vision;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;

/**
 * Zooms limelight on target.
 */
public class CameraZoom extends CommandBase {

    private final Limelight limelight;

    /**
     * Creates a new CameraZoom command to zoom limelight on target.
     *
     * @param limelight - Limelight subsystem to use.
     */
    public CameraZoom(Limelight limelight) {
        this.limelight = limelight;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        limelight.zoomTarget();
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
