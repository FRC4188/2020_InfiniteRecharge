package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

/**
 * Runs magazine and indexer motors to feed cells if shooter is at correct rpm.
 */
public class AutoMagazine extends CommandBase {

    private final Magazine magazine;
    private final Intake intake;
    private final Shooter shooter;
    private final Limelight limelight;
    private final double TOLERANCE = 250;
    private final double TIMEOUT = 20;
    private double timer;
    private boolean loaded;

    /**
     * Constructs a new AutoMagazine command to feed cells if shooter is at correct rpm.
     *
     * @param magazine - Magazine subsystem to use for turning motors.
     * @param intake - Intake subsystem that feeds into the magazine.
     * @param shooter - Shooter subsystem to use for getting current shooter rpm.
     */
    public AutoMagazine(Magazine magazine, Intake intake, Limelight limelight, Shooter shooter) {
        addRequirements(magazine, intake);
        this.magazine = magazine;
        this.intake = intake;
        this.shooter = shooter;
        this.limelight = limelight;
    }

    @Override
    public void initialize() {
        loaded = !magazine.topBeamClear();
        timer = 0;
    }

    @Override
    public void execute() {

        if (loaded && timer < TIMEOUT) {
            intake.spinIndexer(-0.2);
            magazine.set(-0.2);
            timer++;
        }
        else {
            double currentVel = shooter.getLeftVelocity();
            double targetVel = limelight.formulaRpm();

            if (Math.abs(currentVel - targetVel) > TOLERANCE) {
                magazine.set(0);
                intake.spin(0, 0, 0);
            }
            else {
                magazine.set(0.8);
                intake.spin(0.6, 0.7, 1.0);
            }
        }

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        if (!magazine.getManual()) {
            magazine.set(0);
        }
        intake.spin(0, 0, 0);
    }

}