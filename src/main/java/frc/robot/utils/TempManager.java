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
        if (climber.getLeftTemp() > MAX_TEMP) {
            sb.append("C32: " + climber.getLeftTemp() + ", ");
        }
        if (climber.getRightTemp() > MAX_TEMP) {
            sb.append("C31: " + climber.getRightTemp() + ", ");
        }
        if (drivetrain.getLeftMasterTemp() > MAX_TEMP) {
            sb.append("D1: " + drivetrain.getLeftMasterTemp() + ", ");
        }
        if (drivetrain.getRightMasterTemp() > MAX_TEMP) {
            sb.append("D3: " + drivetrain.getRightMasterTemp() + ", ");
        }
        if (drivetrain.getLeftSlaveTemp() > MAX_TEMP) {
            sb.append("D2: " + drivetrain.getLeftSlaveTemp() + ", ");
        }
        if (drivetrain.getRightSlaveTemp() > MAX_TEMP) {
            sb.append("D4: " + drivetrain.getRightSlaveTemp() + ", ");
        }
        if (intake.getIntakeTemp() > MAX_TEMP) {
            sb.append("I11: " + intake.getIntakeTemp() + ", ");
        }
        if (intake.getIndexerTemp() > MAX_TEMP) {
            sb.append("I12: " + intake.getIndexerTemp() + ", ");
        }
        /*if (intake.getPolyRollerTemp() > MAX_TEMP) {
            sb.append("I13: " + intake.getPolyRollerTemp() + ", ");
        }*/
        if (magazine.getTemp() > MAX_TEMP) {
            sb.append("M: " + magazine.getTemp() + ", ");
        }
        if (shooter.getLeftTemp() > MAX_TEMP) {
            sb.append("S21: " + shooter.getLeftTemp() + ", ");
        }
        if (shooter.getRightTemp() > MAX_TEMP) {
            sb.append("S22: " + shooter.getRightTemp() + ", ");
        }
        if (turret.getTemp() > MAX_TEMP) {
            sb.append("T: " + turret.getTemp() + ", ");
        }
        SmartDashboard.putString("Temp Warnings", sb.toString());
    }

}