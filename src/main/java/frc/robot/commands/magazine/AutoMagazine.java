package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

/**
 * Runs magazine motors to feed cells if shooter is at correct rpm.
 */
public class AutoMagazine extends CommandBase {

    private final Magazine magazine;
    private final Limelight limelight;
    private final Shooter shooter;

    /**
     * Constructs a new AutoMagazine command to feed cells if shooter is at correct rpm.
     *
     * @param magazine - Magazine subsystem to use for turning motors.
     * @param limelight - Limelight subsystem to use for determining correct shooter rpm.
     * @param shooter - Shooter subsystem to use for getting current shooter rpm.
     */
    public AutoMagazine(Magazine magazine, Limelight limelight, Shooter shooter) {
        addRequirements(magazine);
        this.magazine = magazine;
        this.limelight = limelight;
        this.shooter = shooter;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {

        // get current shooter velocity and target velocity
        double currentVel = shooter.getLeftVelocity();
        double targetVel = limelight.formulaRpm();

        SmartDashboard.putNumber("Mag-read shooter rpm", currentVel);
        SmartDashboard.putNumber("Mag-read formula rpm", targetVel);

        // feed shooter only if at correct rpm
        if (Math.abs(currentVel - targetVel) > 350) magazine.set(0);
        else magazine.set(0.9);

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        magazine.set(0);
    }
}