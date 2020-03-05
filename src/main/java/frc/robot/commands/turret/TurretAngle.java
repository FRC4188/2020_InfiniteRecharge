package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

public class TurretAngle extends CommandBase {

    private final Turret turret;

    private double counter;
    private double targetPosition;
    private double lastError;

    private static final double kP = 0.0000055;
    private static final double kD = 0.75;
    private static final double DELTA_T = 0.02;
    private static final double TOLERANCE = 3.0; // degrees

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

    /**
     * Constructs a new TurretAngle command to spin turret 360 degrees to face target.
     *
     * @param turret - Turret subsystem to use.
     * @param limelight - Limelight subsystem to use.
     */
    public TurretAngle(Turret turret, Limelight limelight) {
        addRequirements(turret);
        this.turret = turret;
        targetPosition = turret.getPosition() - limelight.getHorizontalAngle() + 
                SmartDashboard.getNumber("Turret Aim adjust", 0.0) - 360 * 
                Math.signum(turret.getPosition() - 180
        );
        if (targetPosition > turret.getMaxPosition() || targetPosition < turret.getMinPosition()) 
            targetPosition = turret.getPosition();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        double position = turret.getPosition();
        double error = targetPosition - position;
        turret.set(kP * error + kD * lastError * DELTA_T);
        lastError = error;
        if (Math.abs(error) < TOLERANCE) counter++;
        else counter = 0;
    }

    @Override
    public boolean isFinished() {
        return counter > 10;
    }

    @Override
    public void end(boolean interrupted) {
        turret.set(0);
    }

}
