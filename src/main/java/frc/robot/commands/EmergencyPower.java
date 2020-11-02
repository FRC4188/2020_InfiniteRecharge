package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.WheelSpinner;

public class EmergencyPower extends CommandBase {

    private final Drivetrain drivetrain;
    private final Shooter shooter;
    private final Turret turret;
    private final Magazine magazine;
    private final Intake intake;
    private final WheelSpinner wheel;

    private boolean cont;

    /**
     * Constructs new ToggleWheel command to fire wheel solenoids.
     * Raises wheel if it is currently lowered and vice versa.
     * @param wheel - Wheel subsystem to use.
     */
    public EmergencyPower(Drivetrain drivetrain, Shooter shooter, Turret turret, Magazine magazine, Intake intake, WheelSpinner wheel, boolean cont) {
        this.drivetrain = drivetrain;
        this.shooter = shooter;
        this.turret = turret;
        this.magazine = magazine;
        this.intake = intake;
        this.wheel = wheel;

        this.cont = cont;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (cont) {
            drivetrain.setReduction(1);
            shooter.setReduction(0);
            turret.setReduction(0);
            magazine.setReduction(0);
            intake.setReduction(0);
            wheel.setReduction(0);
        } else {
            drivetrain.setReduction(1);
            shooter.setReduction(1);
            turret.setReduction(1);
            magazine.setReduction(1);
            intake.setReduction(1);
            wheel.setReduction(1);
        }
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
