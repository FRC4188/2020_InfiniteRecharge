package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class FireIntake extends CommandBase {
    
    Intake intake;
    Value value;

    public FireIntake(Value value, Intake intake) {
        this.intake = intake;
        this.value = value;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.setSolenoid(value);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}