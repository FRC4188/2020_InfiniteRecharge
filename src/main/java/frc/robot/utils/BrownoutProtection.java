package robot.utils;

import edu.wpi.first.wpilibj.RobotController;

/** Motitors battery voltage and provides variable to scale down motor output. */
public class BrownoutProtection {

    double brownoutVar = 1.0;
    int powerLevel = 0;

    double climberPower = 1.0;
    double drivetrainPower = 1.0;
    double intakePower = 1.0;
    double magazinePower = 1.0;
    double shooterPower = 1.0;
    double turretPower = 1.0;
    double wheelPower = 1.0;

    final double MIN_VOLTAGE = 7.2;

    /** Reads battery voltage and adjusts brownout var accordingly. */
    public void run() {
        double voltage = RobotController.getBatteryVoltage();
        if (voltage < MIN_VOLTAGE) ++powerLevel;
        else if (voltage > 10) --powerLevel;
        else brownoutVar = 1.0;
            drivetrainPower = 1.0;
            intakePower = 1.0;
            magazinePower = 1.0;
            shooterPower = 1.0;
            turretPower = 1.0;
            wheelPower = 1.0;
        if (powerLevel == 0) {
            drivetrainPower = 1.0;
            intakePower = 0.7;
            magazinePower = 0.7;
            shooterPower = 1.0;
            turretPower = 0.7;
            wheelPower = 0.7;
        } else if (powerLevel == 2) {
            drivetrainPower = 0.7;
            intakePower = 0.5;
            magazinePower = 0.5;
            shooterPower = 1.0;
            turretPower = 0.5;
            wheelPower = 0.5;
        } else if (powerLevel == 3) {
            drivetrainPower = 0.7;
            intakePower = 0.5;
            magazinePower = 0.5;
            shooterPower = 1.0;
            turretPower = 0.5;
            wheelPower = 0.5;
        } else {
            drivetrainPower = 0.5;
            intakePower = 0.3;
            magazinePower = 0.3;
            shooterPower = 1.0;
            turretPower = 0.3;
            wheelPower = 0.3;
        }
    }

    /** Returns current brownout variable based on battery voltage. */
    public double getBrownoutVar() {
        return brownoutVar;
    }
}