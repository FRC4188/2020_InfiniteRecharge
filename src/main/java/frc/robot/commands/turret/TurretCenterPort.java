package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

/**
 * Turns turret to keep vision target centered.
 */
public class TurretCenterPort extends CommandBase {

    private final Turret turret;
    private final Limelight limelight;

    private final double kP = 0.05;
    private final double kD = 0.0;
    private final PIDController pid = new PIDController(kP, 0, kD);

    /**
     * Constructs new TurretCenterPort command to turn turret to keep vision target centered.
     *
     * @param turret - Turret subsystem to use.
     * @param limelight - Limelight subsystem to use.
     */
    public TurretCenterPort(Turret turret, Limelight limelight) {
        addRequirements(turret);
        this.turret = turret;
        this.limelight = limelight;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        double position = turret.getPosition();
        double setpoint = limelight.getHorizontalAngle();
        double output = pid.calculate(position, setpoint);
        turret.set(output);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }

}
