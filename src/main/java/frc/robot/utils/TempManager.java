package frc.robot.utils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

/** Display temperatures of motors accessed by CAN ID. */
public class TempManager {

    private static final int MAX_TEMP = 50; // cel
    private static final int FALCON_TEMP = 50; // cel
    private boolean cool;

    private final Climber climber;
    private final Drivetrain drivetrain;
    private final Intake intake;
    private final Magazine magazine;
    private final Shooter shooter;
    private final Turret turret;

    /**
     * Takes all subsystems with motors as parameters and makes a TempManager object.
     */
    public TempManager(Climber climber, Drivetrain drivetrain,
            Intake intake, Magazine magazine, Shooter shooter, Turret turret) {
        this.climber = climber;
        this.drivetrain = drivetrain;
        this.intake = intake;
        this.magazine = magazine;
        this.shooter = shooter;
        this.turret = turret;
    }

    /**
     * Gets temperature of every motor and writes to Smart Dash in C if temp is over a max temp.
     */
    public void run() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 31; i < 32; i++) {
            if (climber.getMotorTemperature(i) > MAX_TEMP) {
                final double tempInC = climber.getMotorTemperature(i);
                sb.append("C" + i + ": " + tempInC + ", ");
            }
        }
        for (int i = 0; i < 4; i++) {
            if (drivetrain.getMotorTemperature(i) > MAX_TEMP) {
                final double tempInC = drivetrain.getMotorTemperature(i);
                // add here to do pneumatic cooling
                sb.append("D" + i + ": " + tempInC + ", ");
            }
        }
        for (int i = 11; i < 13; i++) {
            if (intake.getMotorTemperature(i) > MAX_TEMP) {
                final double tempInC = intake.getMotorTemperature(i);
                sb.append("I" + i + ": " + tempInC + ", ");
            }
        }
        if (magazine.getMotorTemperature() > MAX_TEMP) {
            final double tempInC = magazine.getMotorTemperature();
            sb.append("M" + 24 + ": " + tempInC + ", ");
        }
        for (int i = 26; i < 27; i++) {
            if (shooter.getMotorTemperature(i) > MAX_TEMP) {
                final double tempInC = shooter.getMotorTemperature(i);
                //add here to do pneumatic cooling
                sb.append("S" + i + ": " + tempInC + ", ");
            }
        }
        if (turret.getMotorTemperature() > MAX_TEMP) {
            final double tempInC = turret.getMotorTemperature();
            sb.append("T" + 23 + ": " + tempInC + ", ");
        }
        SmartDashboard.putString("Temp Warnings", sb.toString());
    }

}