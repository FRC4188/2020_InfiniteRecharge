package frc.robot.commands.wheel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.WheelSpinner;

/**
 * Sets limelight to track target.
 */
public class SpinWheel extends CommandBase {

    private final WheelSpinner wheelSpinner;
    private double percent;

    /**
     * Constructs a new SpinWheel command to set limelight to track target at close distances.
     *
     * @param wheelSpinner - WheelSpinner subsystem to use.
     */
    public SpinWheel(WheelSpinner wheelSpinner, double percent) {
        this.wheelSpinner = wheelSpinner;
        this.percent = percent;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        wheelSpinner.setPercentage(percent);
    }

    @Override
    public void end(boolean interrupted) {
        wheelSpinner.setPercentage(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
