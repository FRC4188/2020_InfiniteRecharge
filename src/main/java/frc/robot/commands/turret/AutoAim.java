package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

public class AutoAim extends CommandBase {

    Turret turret;
    Limelight limelight;
    private final double kP = 1.0 / 47;

    /**
     * Constructs a new AutoAim command to center turret on vision target.
     *
     * @param turret - Turret subsystem to use.
     * @param limelight - Limelight subsystem to use.
     */
    public AutoAim(Turret turret, Limelight limelight) {
        addRequirements(turret);
        this.turret = turret;
        this.limelight = limelight;
        limelight.trackTarget();
    }

    @Override
    public void initialize() {
        SmartDashboard.putNumber("Turret kP adjust", 0.0);
    }

    @Override
    public void execute() {
        turret.set(-limelight.getHorizontalAngle() * kP
                + SmartDashboard.getNumber("Turret kP adjust", 0.0) / 10.0);
    }

    @Override
    public void end(boolean interrupted) {
        turret.set(0);
    }

    @Override
    public boolean isFinished() {
        return (turret.getPosition() <= turret.getMinPosition()
                || turret.getPosition() >= turret.getMaxPosition());
    }

}
