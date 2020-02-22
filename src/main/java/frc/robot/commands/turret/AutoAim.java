package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Limelight.CameraMode;
import frc.robot.subsystems.Limelight.LedMode;

/**
 * Turns turret to keep vision target centered.
 */
public class AutoAim extends CommandBase {

    private final Turret turret;
    private final Limelight limelight;
    private double adjust;

    /**
     * Constructs new AutoAim command to turn turret to keep vision target centered.
     *
     * @param turret - Turret subsystem to use.
     * @param limelight - Limelight subsystem to use.
     */
    public AutoAim(Turret turret, Limelight limelight) {
        addRequirements(turret);
        this.turret = turret;
        this.limelight = limelight;
        SmartDashboard.putNumber("Turret Aim adjust", 0.0);
    }

    @Override
    public void initialize() {
        limelight.setLightMode(LedMode.ON);
    }

    @Override
    public void execute() {
        // simple P loop
        adjust = SmartDashboard.getNumber("Turret Aim adjust", 0.0);
        turret.set((-limelight.getHorizontalAngle() + adjust) / 47.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        turret.set(0);
    }

}
