package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;

public class TurnBelt extends CommandBase{

    private Magazine magazine;
    private double velocity; // [-1.0, 1.0]
    
    public TurnBelt(Magazine magazine, double velocity) {
        addRequirements(magazine);
        this.magazine = magazine;
        this.velocity = velocity;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        magazine.set(velocity);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        magazine.set(0);
    }
}