package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Turret;

public class TurretAngle extends CommandBase {

    private final Turret turret;

    double targetPosition;

    private static final double D_TOLERANCE = 0.25; // degrees
    private static final double V_TOLERANCE = 0.5; // degrees per second

    /**
     * Constructs a new TurretAngle command to spin turret to a given number degree.
     *
     * @param turret - Turret subsystem to use.
     * @param targetPosition - Position to set the turret to.
     */
    public TurretAngle(Turret turret, double targetPosition) {
        addRequirements(turret);
        this.turret = turret;
        this.targetPosition = targetPosition;
    }

    @Override
    public void initialize() {
        //nothing upon intialization
    }

    @Override
    public void execute() {
        turret.setAngle(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(turret.getPosition()) < D_TOLERANCE && Math.abs(turret.getVelocity()) < V_TOLERANCE;
    }

    @Override
    public void end(boolean interrupted) {
        turret.set(0);
    }

}
