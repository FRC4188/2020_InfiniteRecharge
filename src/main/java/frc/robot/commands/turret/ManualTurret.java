package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Turret;

/**
 * Manually sets turret to a given output.
 */
public class ManualTurret extends CommandBase {

    private final Turret turret;
    private final double percent;

    /**
     * Constructs a new ManualTurret command to set turret to a given output.
     *
     * @param turret - Turret subsystem to use.
     * @param percent - percent output to command turret motors [-1.0, 1.0].
     */
    public ManualTurret(Turret turret, double percent) {
        addRequirements(turret);
        this.turret = turret;
        this.percent = percent;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        /*
        if ((turret.getPosition() >= turret.getMaxPosition() && percent > 0)
                || (turret.getPosition() <= turret.getMinPosition() && percent < 0)) {
            turret.set(0);
        } else {
            turret.set(percent);
        }
        */
        turret.set(percent);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }

}
