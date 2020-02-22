package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

public class Spin360 extends CommandBase {

    private final Turret turret;
    private final Limelight limelight;

    private double counter;
    private double targetPosition;
    private double lastError;

    private static final double kP = 0.0000055;
    private static final double kD = 0.75;
    private static final double DELTA_T = 0.02;
    private static final double TOLERANCE = 3.0; // degrees

    /**
     * Constructs a new Spin360 command to spin turret 360 degrees.
     *
     * @param turret - Turret subsystem to use.
     * @param limelight - Limelight subsystem to use.
     */
    public Spin360(Turret turret, Limelight limelight) {
        addRequirements(turret);
        this.turret = turret;
        this.limelight = limelight;
    }

    @Override
    public void initialize() {
        targetPosition = turret.getPosition() - limelight.getHorizontalAngle() + SmartDashboard.getNumber("Turret Aim adjust", 0.0) 
                - 360 * Math.signum(turret.getPosition() - 180);
        if (targetPosition > turret.getMaxPosition() || targetPosition < turret.getMinPosition()) targetPosition = turret.getPosition();
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
