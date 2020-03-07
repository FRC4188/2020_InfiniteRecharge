package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Turret;

/**
 * Turns turret to keep vision target centered.
 */
public class ZeroTurret extends CommandBase {

    private final Turret turret;

    /**
     * Constructs new TurretCenterPort command to turn turret to keep vision target centered.
     *
     * @param turret - Turret subsystem to use.
     */
    public ZeroTurret(Turret turret) {
        addRequirements(turret);
        this.turret = turret;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        turret.resetEncoders();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }

}
