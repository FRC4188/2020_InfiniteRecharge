package frc.robot.commands.wheel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.WheelSpinner;

public class LowerWheel extends CommandBase {

    private final WheelSpinner wheel;

    /**
     * Constructs new ToggleWheel command to fire wheel solenoids.
     * Raises wheel if it is currently lowered and vice versa.
     * @param wheel - Wheel subsystem to use.
     */
    public LowerWheel(WheelSpinner wheel) {
        addRequirements(wheel);
        this.wheel = wheel;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        wheel.lower();
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
