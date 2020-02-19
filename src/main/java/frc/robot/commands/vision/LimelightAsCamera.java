package frc.robot.commands.vision;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;

/**
 * Turns off vision tracking for limelight.
 */
public class LimelightAsCamera extends CommandBase {

    Limelight limelight;

    /**
     * Constructs a new LimelightAsCamera command to stop vision tracking.
     *
     * @param limelight - Limelight subsystem to use.
     */
    public LimelightAsCamera(Limelight limelight) {
        this.limelight = limelight;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        limelight.useAsCamera();
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
