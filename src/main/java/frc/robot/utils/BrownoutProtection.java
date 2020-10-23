package frc.robot.utils;

import edu.wpi.first.wpilibj.RobotController;

/** Motitors battery voltage and provides variable to scale down motor output. */
public class BrownoutProtection {

    boolean isEmergency = false;
    int powerLevel = 0;

    double climberPower = 1.0;
    double drivetrainPower = 1.0;
    double intakePower = 1.0;
    double magazinePower = 1.0;
    double shooterPower = 1.0; //TODO: PID power
    double turretPower = 1.0; //TODO: PID power
    double wheelPower = 1.0;

    final double MIN_VOLTAGE = 7.2;

    /** Reads battery voltage and adjusts brownout var accordingly. */
    public void run() {
        double voltage = RobotController.getBatteryVoltage();
        if (voltage < MIN_VOLTAGE) ++powerLevel;
        else if (voltage > 10) --powerLevel;
        else {
            drivetrainPower = 1.0;
            intakePower = 1.0;
            magazinePower = 1.0;
            shooterPower = 1.0;
            turretPower = 1.0;
            wheelPower = 1.0;
        }

        if (isEmergency) {
            drivetrainPower = 1.0;
            intakePower = 0.0;
            magazinePower = 0.0;
            shooterPower = 0.0;
            turretPower = 0.0;
            wheelPower = 0.0;
        } else if (powerLevel == 0) {
            drivetrainPower = 1.0;
            intakePower = 0.7;
            magazinePower = 1.0; //0.7;
            shooterPower = 1.0;
            turretPower = 0.7;
            wheelPower = 0.7;
        } else if (powerLevel == 1) {
            drivetrainPower = 0.7;
            intakePower = 0.5;
            magazinePower = 1.0; //0.5;
            shooterPower = 1.0;
            turretPower = 0.5;
            wheelPower = 0.5;
        } else if (powerLevel == 2) {
            drivetrainPower = 0.7;
            intakePower = 0.5;
            magazinePower = 1.0; //0.5;
            shooterPower = 1.0;
            turretPower = 0.5;
            wheelPower = 0.5;
        } else {
            drivetrainPower = 0.5;
            intakePower = 0.3;
            magazinePower = 1.0; //0.3;
            shooterPower = 1.0;
            turretPower = 0.3;
            wheelPower = 0.3;
        }
    }

    public double getDrivetrainPower() {
        return drivetrainPower;
    }

    public double getIntakePower() {
        return intakePower;
    }

    public double getMagazinePower() {
        return magazinePower;
    }

    public double getShooterPower() {
        return shooterPower;
    }

    public double getTurretPower() {
        return turretPower;
    }

    public double getWheelPower() {
        return wheelPower;
    }

    public void setEmergencyPower(boolean isEmergency) {
        this.isEmergency = isEmergency;
    }
}