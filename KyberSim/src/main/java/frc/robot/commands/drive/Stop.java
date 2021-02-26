package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DiffDrive;

public class Stop extends CommandBase {

    private DiffDrive drivetrain;

    public Stop(DiffDrive drivetrain) {
        addRequirements(drivetrain);

        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        drivetrain.drive(0.0, 0.0);
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}