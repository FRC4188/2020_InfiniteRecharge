package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class FireIntake extends CommandBase {
    
    Intake intake;
    boolean output;

    public FireIntake(Intake intake) {
        this.intake = intake;
        //output = intake.getSolenoid();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        //intake.setSolenoid(false);
        intake.setSolenoid(!output);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}