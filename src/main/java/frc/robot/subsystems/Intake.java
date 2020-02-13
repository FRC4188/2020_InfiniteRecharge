package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Robot;

public class Intake extends SubsystemBase {
    private CANSparkMax intakeMotor = new CANSparkMax(24, MotorType.kBrushless);
    private CANSparkMax indexerMotor = new CANSparkMax(12, MotorType.kBrushless);
    private CANSparkMax polyRoller = new CANSparkMax(31, MotorType.kBrushless);
    private DoubleSolenoid intakeSolenoid;
    //private DoubleSolenoid intakeSolenoid = new DoubleSolenoid(0,1);
    /*private DigitalInput intakeBB1 = new DigitalInput(0);
    private DigitalInput intakeBB2 = new DigitalInput(1);
    private DigitalInput magazineBB = new DigitalInput(2);
    private boolean lsRelease = true;
    private boolean lsRelease2 = true;
    private boolean bbRelease = false;*/

    public static int ballCount = 0;
    private boolean intakeInverted = false;
    private boolean indexerInverted = false;

    /**
     * Constructs new Intake object.
     */
    public Intake() {
    }

    /** Runs every loop. */
    @Override
    public void periodic() {

      /**
       * Adds to ballCount if the limit switch is clicked.
       */

      /*if(intakeBB1.get() != lsRelease) {
        if(intakeBB1.get() == true) {
          lsRelease = true;
        }
        else {
          ballCount++;
          System.out.println(ballCount);
          lsRelease = false;
        }
      }
      else if(intakeBB2.get() != lsRelease2) {
        if(intakeBB2.get() == true) {
          lsRelease2 = true;
        }
        else {
          ballCount++;
          System.out.println(ballCount);
          lsRelease2 = false;
        }
      }
      else if(magazineBB.get() != bbRelease) {
          if(magazineBB.get() == false) {
              bbRelease = false;
          }
          else {
              ballCount++;
              System.out.println(ballCount);
              bbRelease = true;
          }
      }
      else {}


      System.out.println(ballCount); */
    }

    public void updateShuffleboard() {
        SmartDashboard.putNumber("Balls", ballCount);
        SmartDashboard.putNumber("I11 Temp", intakeMotor.getMotorTemperature());
        //SmartDashboard.putNumber("I12 Temp", indexer1.getMotorTemperature());
        //SmartDashboard.putNumber("I13 Temp", indexer2.getMotorTemperature());
    }

    /**
     * Deploys intake solenoid.
     */
    public void setSolenoid(Value value) {
        intakeSolenoid.set(value);
        intakeSolenoid.set(Value.kOff);
    }

    /**
     * Spins the intake motor based on a given percent.
     * The motor only spins if the number of balls is less 
     * than 0.
     */
    public void spinIntake(double percent) {
       // if(ballCount < 5) {
            intakeMotor.set(percent);
       // }
    }

    /**
     * Spins the indexer motors together.
     * They only spin if the number of balls is less than
     * or equal to 0.
     */
    public void spinIndexer(double percent) {
      System.out.println("Spin index");
        indexerMotor.set(percent);
    }

    public void spinIndexer2(double percent) {
      System.out.println("spin index 2");
      indexerMotor.set(percent);
    }

    /**
     * Spin poly rollers.
     */
    public void spinPolyRollers(double percent) {
        polyRoller.set(percent);
    }

    /**
     * Inverts the intake motors.
     */
    public void setIntakeInverted(boolean isInverted) {
        if(intakeInverted) isInverted = !isInverted;
        intakeMotor.setInverted(isInverted);
    }

    /**
     * Inverts the indexer motors.
     */
    public void setIndexerInverted(boolean isInverted) {
        if(indexerInverted) isInverted = !isInverted;
        //indexer1.setInverted(isInverted);
        //indexer2.setInverted(isInverted);
    }

    /**
     * Manually changes the ball count.
     */
    public void changeBallCount(int value) {
      ballCount += value;
    }

    /*public boolean indexerInterval() {
      return (intakeBB1.get() || intakeBB2.get()) && magazineBB.get();
    }

    public boolean magazineGetter() {
      return magazineBB.get();
    }*/

    public int getBallCount() {
      return ballCount;
    }
}