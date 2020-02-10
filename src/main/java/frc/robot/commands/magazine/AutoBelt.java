package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;

public class AutoBelt extends CommandBase{

    private Magazine magazine;
    
    public AutoBelt(Magazine magazine) {
        addRequirements(magazine);
        this.magazine = magazine;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        magazine.set(magazine.getSpeed());
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