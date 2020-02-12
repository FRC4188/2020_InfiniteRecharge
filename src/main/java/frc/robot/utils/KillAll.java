package frc.robot.utils;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * Kills all currently running processes on the robot.
 */
public class KillAll extends CommandBase {

    @Override
    public void execute() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}