package frc.robot.commands.hood;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;

public class FireHood extends CommandBase {
    
    Hood hood;
    boolean output;

    public FireHood(Hood hood, boolean output) {
        addRequirements(hood);
        this.hood = hood;
        this.output = output;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        hood.setHood(output);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return true;
    }
}
