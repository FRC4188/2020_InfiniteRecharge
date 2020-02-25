package frc.robot.commands.vision;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;

/**
 * Turns off vision tracking to use limelight as regular camera.
 */
public class UseAsCamera extends CommandBase {

    private final Limelight limelight;

    /**
     * Constructs a new UseAsCamera command to use limelight as a regular camera.
     *
     * @param limelight - Limelight subsystem to use.
     */
    public UseAsCamera(Limelight limelight) {
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
