package frc.robot.commands.turret;

import frc.robot.subsystems.Turret;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** Manually controls turret using copilot triggers (left up, right down). */
public class ManualTurret extends CommandBase {

    Turret turret;
    public double output;

    public ManualTurret(Turret turret, double output) {
        addRequirements(turret);
        this.turret = turret;
        this.output = output;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (Math.abs(output) > 0) turret.set(output);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }

}