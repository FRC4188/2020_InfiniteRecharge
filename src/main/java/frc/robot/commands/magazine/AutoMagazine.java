package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;

/**
 * Runs magazine and indexer motors to feed cells if shooter is at correct rpm.
 */
public class AutoMagazine extends CommandBase {

    private final Magazine magazine;
    private final Intake intake;
    private boolean top;
    private boolean mid;
    private boolean cont;
    private boolean forw;

    /**
     * Constructs a new AutoMagazine command to feed cells if shooter is at correct rpm.
     *
     * @param magazine Magazine subsystem to use for turning motors.
     * @param intake Intake subsystem that feeds into the magazine.
     * @param forward Whether the system is taking balls through the regular track or reversed.
     * @param cont Whether the command is starting or stopping.
     */
    public AutoMagazine(Magazine magazine, Intake intake, boolean forward, boolean cont) {
        addRequirements(magazine, intake);
        this.magazine = magazine;
        this.intake = intake;
        this.cont = cont;
        this.forw = forward;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        top = magazine.topBeamClear();
        mid = magazine.midBeamClear();

        if (cont) {
            if (forw) {
                if (!mid && top) {
                    intake.spin(-1.0, -1.0);
                    magazine.set(0.35);
                }
                else if (mid && top) {
                    magazine.set(0.0);
                    intake.spin(-1.0, -1.0);
                }
                else if (!top) {
                    magazine.set(0.0);
                    intake.spin(-1.0,0.0);
                }
            } else {
                if (!mid && top) {
                    intake.spin(1.0, 1.0);
                    magazine.set(-0.1);
                }
                else if (mid && top) {
                    magazine.set(0.0);
                    intake.spin(1.0, 0.5);
                }
                else if (!top) {
                    magazine.set(-0.35);
                    intake.spin(1.0,0.0);
                }
            }
        }else {
            magazine.set(0.0);
            intake.spin(0.0,0.0);
        }
    }


    @Override
    public boolean isFinished() {
        return !cont;
    }

    @Override
    public void end(boolean interrupted) {

        // Upon stop intake stops and if the magazine is being controlled automatically it will stop.
        if (!magazine.getManual()) {
            magazine.set(0.0);
        }
        intake.spin(0.0, 0.0);
    }

}