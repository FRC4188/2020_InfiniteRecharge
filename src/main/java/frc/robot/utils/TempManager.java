package frc.robot.utils;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Display temperatures of motors accessed by CAN ID. */
public class TempManager {

    final int MAX_TEMP = 55; //Cel

    Climber climber = new Climber();
    Drivetrain drivetrain = new Drivetrain();
    Intake intake = new Intake();
    Magazine magazine = new Magazine();
    Shooter shooter = new Shooter();
    Turret turret = new Turret();

    /** Gets temperature of every motor and writes to Smart Dash in F if temp is over a max temp. */
    public void run() {
        StringBuilder sb = new StringBuilder();

        for(int i = 31; i <= 32; i++){
            if(climber.getMotorTemperature(i) > MAX_TEMP){
                double tempInC = climber.getMotorTemperature(i);
                sb.append("C" + i + ": " + tempInC + ", ");
            }
        }
        for(int i = 1; i <= 4; i++){
            if(drivetrain.getMotorTemperature(i) > MAX_TEMP){
                double tempInC = drivetrain.getMotorTemperature(i);
                // add here to do pneumatic cooling
                sb.append("D" + i + ": " + tempInC + ", ");
            }
        }
        for(int i = 11; i <= 13; i++){
            if(intake.getMotorTemperature(i) > MAX_TEMP){
                double tempInC = intake.getMotorTemperature(i);
                sb.append("I" + i + ": " + tempInC + ", ");
            }
        }
        for(int i = 24; i <= 24; i++){
            if(magazine.getMotorTemperature(i) > MAX_TEMP){
                double tempInC = magazine.getMotorTemperature(i);
                sb.append("M" + i + ": " + tempInC + ", ");
            }
        }
        for(int i = 26; i <= 27; i++){
            if(shooter.getMotorTemperature(i) > MAX_TEMP){
                double tempInC = shooter.getMotorTemperature(i);
                //add here to do pneumatic cooling
                sb.append("S" + i + ": " + tempInC + ", ");
            }
        }
        for(int i = 23; i <= 23; i++){
            if(turret.getMotorTemperature(i) > MAX_TEMP){
                double tempInC = turret.getMotorTemperature(i);
                sb.append("T" + i + ": " + tempInC + ", ");
            }
        }
        SmartDashboard.putString("Temp Warnings", sb.toString());
    }

}