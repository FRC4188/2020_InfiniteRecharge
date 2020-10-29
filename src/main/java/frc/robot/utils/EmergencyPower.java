package frc.robot.utils;

import frc.robot.utils.CspController;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

/**
 * Sets the emergency power on the robot.
 */
public class EmergencyPower {

    private final CspController controller;
    private final Drivetrain drivetrain;
    private final Intake intake;
    private final Magazine magazine;
    private final Shooter shooter;
    private final Turret turret;

    /**
     * Constructs new EmergencyPower command to engage the emergency power.
     */
    public EmergencyPower(CspController controller, Drivetrain drivetrain, Intake intake, Magazine magazine, Shooter shooter, Turret turret) {
        this.controller = controller; //TODO: make it so that the controller will tell the EmergencyPower when to activate
        this.drivetrain = drivetrain;
        this.intake = intake;
        this.magazine = magazine;
        this.shooter = shooter;
        this.turret = turret;
    }

    public void run() {
        if (controller.getEmergencyPower()) {
            drivetrain.setReduction(1.0);
            intake.setReduction(0.0);
            magazine.setReduction(0.0);
            shooter.setReduction(0.0);
            turret.setReduction(0.0);
        } // This will overwrite the BrownoutProtection
        //controller.setEmergencyPower(this.isEmergency);
    }
}