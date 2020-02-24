package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

/**
 * Runs magazine motors to feed cells if shooter is at correct rpm.
 */
public class AutoMagazine extends CommandBase {

    private final Magazine magazine;
    private final Intake intake;
    private final Limelight limelight;
    private final Shooter shooter;

    /**
     * Constructs a new AutoMagazine command to feed cells if shooter is at correct rpm.
     *
     * @param magazine - Magazine subsystem to use for turning motors.
     * @param limelight - Limelight subsystem to use for determining correct shooter rpm.
     * @param shooter - Shooter subsystem to use for getting current shooter rpm.
     */
    public AutoMagazine(Magazine magazine, Intake intake, Limelight limelight, Shooter shooter) {
        addRequirements(magazine, intake);
        this.magazine = magazine;
        this.intake = intake;
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

        // feed shooter only if at correct rpm
        if (Math.abs(currentVel - targetVel) > 50) {
            magazine.set(0);
            intake.spinIndexer(0);
            intake.spinPolyRollers(0);
        }
        else {
            magazine.set(0.9);
            intake.spinIndexer(0.9);
            intake.spinPolyRollers(0.9);
        }

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
