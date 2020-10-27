package frc.robot.utils;

import edu.wpi.first.wpilibj.RobotController;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class BrownoutProtection {

    Drivetrain drivetrain;
    Intake intake;
    Magazine magazine;
    Shooter shooter;
    Turret turret;

    private static final double MID_VOLTAGE = 8.0;
    private static final double MIN_VOLTAGE = 6.0;

    public BrownoutProtection(Drivetrain drivetrain, Intake intake, Magazine magazine, Shooter shooter, Turret turret) {
        this.drivetrain = drivetrain;
        this.intake = intake;
        this.magazine = magazine;
        this.shooter = shooter;
        this.turret = turret;
    }

    public void run() {
        double volts = RobotController.getBatteryVoltage();

        if (volts < MIN_VOLTAGE) {
            drivetrain.setReduction(0.75);
            intake.setReduction(0.0);
            magazine.setReduction(0.0);
            shooter.setReduction(0.0);
            turret.setReduction(0.0);
        } else if (volts < MID_VOLTAGE) {
            drivetrain.setReduction(0.75);
            intake.setReduction(0.8);
            magazine.setReduction(0.6);
            shooter.setReduction(1.0);
            turret.setReduction(1.0);
        } else {
            drivetrain.setReduction(1.0);
            intake.setReduction(1.0);
            magazine.setReduction(1.0);
            shooter.setReduction(1.0);
            turret.setReduction(1.0);
        }
    }
}