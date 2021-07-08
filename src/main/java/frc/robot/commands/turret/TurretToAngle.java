package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Turret;

/**
 * Turns turret to a given angle in degrees.
 */
public class TurretToAngle extends CommandBase {

    private final Turret turret;
    private final double angle;

    private static final double POS_TOLERANCE = 2.0; // degrees
    private static final double VEL_TOLERANCE = 1.0; // degrees per sec

    /**
     * Constructs new TurretToAngle command to turn turret to a given angle in degrees.
     *
     * @param turret - Turret subsystem to use.
     * @param angle - angle to turn to in degrees.
     */
    public TurretToAngle(Turret turret, double angle) {
        addRequirements(turret);
        this.turret = turret;
        this.angle = angle;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        turret.setAngle(angle);
    }

    @Override
    public boolean isFinished() {
        return (Math.abs(turret.getPosition() - angle) < POS_TOLERANCE)
                && (Math.abs(turret.getVelocity()) < VEL_TOLERANCE);
    }

    @Override
    public void end(boolean interrupted) {
        turret.set(0);
    }

}