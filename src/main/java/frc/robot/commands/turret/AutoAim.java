package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

/**
 * Turns turret to keep vision target centered.
 */
public class AutoAim extends CommandBase {

    private final Turret turret;
    private final Limelight limelight;
    private boolean cont;

    /**
     * Constructs new AutoAim command to turn turret to keep vision target centered.
     *
     * @param turret - Turret subsystem to use.
     * @param limelight - Limelight subsystem to use.
     */
    public AutoAim(Turret turret, Limelight limelight, boolean cont) {
        addRequirements(turret);
        this.turret = turret;
        this.limelight = limelight;
        this.cont = cont;
    }

    @Override
    public void initialize() {
        limelight.trackTarget();
    }

    @Override
    public void execute() {
        turret.trackTarget(limelight.getHorizontalAngle());
    }

    @Override
    public boolean isFinished() {
        return (!cont);
    }

    @Override
    public void end(boolean interrupted) {
        turret.set(0);
    }

}