package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utils.CspController;

/**
 * Sets the emergency power on the robot
 */
public class EmergencyPower extends CommandBase {

    private final CspController controller;

    /**
     * Constructs new EmergencyPower command to engage the emergency power
     *
     * @param controller - Controller to use.
     */
    public FireBrake(CspController controller, boolean isEmergency) {
        this.controller = controller;
        this.isEmergency = isEmergency;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        controller.setEmergencyPower(isEmergency);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
